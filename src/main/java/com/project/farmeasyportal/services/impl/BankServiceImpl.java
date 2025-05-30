package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.constants.UsersConstants;
import com.project.farmeasyportal.dao.*;
import com.project.farmeasyportal.entities.*;
import com.project.farmeasyportal.enums.Status;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.*;
import com.project.farmeasyportal.services.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BankServiceImpl implements BankService {

    private final SchemeDao schemeDao;
    private final BankDao bankDao;
    private final ApplyDao applyDao;
    private final UserDao userDao;
    private final FarmerDao farmerDao;
    private final NotificationService notificationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final GrievencesDao grievencesDao;

    @Override
    public BankDTO addBank(BankDTO bankDTO) {
        if (bankDTO.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(bankDTO.getPassword());

        bankDTO.setPassword(encodedPassword);
        bankDTO.setId(String.valueOf(UUID.randomUUID()));

        Bank bank = this.modelMapper.map(bankDTO, Bank.class);

        User user = new User();
        user.setEmail(bank.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(UsersConstants.ROLE_BANK);
        userDao.save(user);
        Bank save = this.bankDao.save(bank);
        return this.modelMapper.map(save, BankDTO.class);
    }

    @Override
    public BankDTO getBankById(String bankId) {
        Bank bank = this.bankDao.findById(bankId).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.BANK, UsersConstants.ID, bankId));
        return this.modelMapper.map(bank, BankDTO.class);
    }

    @Override
    public BankDTO getBankByEmail(String email) {
        Bank bank = this.bankDao.findByEmail(email);
        return this.modelMapper.map(bank, BankDTO.class);
    }

    @Override
    public SchemeDTO addScheme(SchemeDTO schemeDTO, String bankId) {
        Scheme scheme = modelMapper.map(schemeDTO, Scheme.class);
        Bank bank = this.bankDao.findById(bankId).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.BANK, UsersConstants.ID, bankId));

        scheme.setBankId(bank.getId());
        Scheme save = schemeDao.save(scheme);
        return this.modelMapper.map(save, SchemeDTO.class);
    }

    @Override
    public SchemeDTO updateScheme(SchemeDTO schemeDTO, Integer schemeId) {
        Scheme scheme = this.schemeDao.findById(schemeId).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.SCHEME, UsersConstants.ID, String.valueOf(schemeId)));

        scheme.setSchemeName(schemeDTO.getSchemeName());
        scheme.setSchemeCode(schemeDTO.getSchemeCode());
        scheme.setSchemeDescription(schemeDTO.getSchemeDescription());
        scheme.setBenefits(schemeDTO.getBenefits());
        scheme.setEligibility(schemeDTO.getEligibility());
        scheme.setMin_salary(schemeDTO.getMin_salary());
        scheme.setMax_salary(schemeDTO.getMax_salary());
        scheme.setCibil_score(schemeDTO.getCibil_score());
        scheme.setDocuments(schemeDTO.getDocuments());
        scheme.setRoi(schemeDTO.getRoi());
        scheme.setTenure(schemeDTO.getTenure());
        scheme.setSchemeType(schemeDTO.getSchemeType());

        Scheme update = schemeDao.save(scheme);
        return this.modelMapper.map(update, SchemeDTO.class);
    }

    @Override
    public List<BankDTO> getBanks() {
        List<Bank> bankList = this.bankDao.findAll();
        return bankList.stream().map(bank -> this.modelMapper.map(bank, BankDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Boolean isBankExistById(String bankId) {
        return this.bankDao.existsById(bankId);
    }

    @Override
    public Boolean isBankExistByEmail(String email) {
        return this.bankDao.existsByEmail(email);
    }

    @Override
    public List<SchemeDTO> getSchemesByBank(String username) {
        List<Scheme> schemes = schemeDao.findAllByBankId(bankDao.findByEmail(username).getId());
        return schemes.stream().map(scheme -> {
            SchemeDTO schemeDTO = this.modelMapper.map(scheme, SchemeDTO.class);
            Bank bank = this.bankDao.findById(scheme.getBankId()).orElseThrow(() ->
                    new ResourceNotFoundException(UsersConstants.BANK, UsersConstants.ID, scheme.getBankId()));
            BankDTO bankDTO = this.modelMapper.map(bank, BankDTO.class);
            schemeDTO.setBankDTO(bankDTO);
            return schemeDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SchemeDTO> getSchemes() {
        return this.schemeDao.findAll().stream().map(scheme -> {
            SchemeDTO schemeDTO = this.modelMapper.map(scheme, SchemeDTO.class);
            Bank bank = this.bankDao.findById(scheme.getBankId()).orElseThrow(() ->
                    new ResourceNotFoundException(UsersConstants.BANK, UsersConstants.ID, scheme.getBankId()));
            BankDTO bankDTO = this.modelMapper.map(bank, BankDTO.class);
            schemeDTO.setBankDTO(bankDTO);
            return schemeDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ApplyDTO> getApplies() {
        return applyDao.findAll().stream().map(apply ->
                this.modelMapper.map(apply, ApplyDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ApplyDTO> getApplyByBank(String bankId) {
        List<Status> excludedStatuses = List.of(Status.DEACTIVATED);
        List<Apply> applyList = this.applyDao.findAllByBankIdAndStatusNotIn(bankId, excludedStatuses);
        return applyList.stream().map(apply -> {
            ApplyDTO applyDTO = this.modelMapper.map(apply, ApplyDTO.class);
            Farmer farmer = this.farmerDao.findById(apply.getFarmerId()).orElseThrow(() ->
                    new ResourceNotFoundException(UsersConstants.FARMER, UsersConstants.ID, apply.getFarmerId()));
            Bank bank = this.bankDao.findById(bankId).orElseThrow(() ->
                    new ResourceNotFoundException(UsersConstants.BANK, UsersConstants.ID, bankId));
            Scheme scheme = this.schemeDao.findById(apply.getSchemeId()).orElseThrow(() ->
                    new ResourceNotFoundException(UsersConstants.SCHEME, UsersConstants.ID, String.valueOf(apply.getSchemeId())));

            applyDTO.setStatus(String.valueOf(apply.getStatus()));
            applyDTO.setFarmerDTO(this.modelMapper.map(farmer, FarmerDTO.class));
            applyDTO.setBankDTO(this.modelMapper.map(bank, BankDTO.class));
            applyDTO.setSchemeDTO(this.modelMapper.map(scheme, SchemeDTO.class));
            return applyDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateApply(Integer applyId, ApplyUpdateDTO applyUpdateDTO) {
        Apply apply = this.applyDao.findById(applyId).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.APPLY, UsersConstants.ID, String.valueOf(applyId)));

        Status status = Status.fromCode(applyUpdateDTO.getStatus());

        apply.setStatus(status);
        apply.setReview(applyUpdateDTO.getReview());
        apply.setStatusDate(String.valueOf(LocalDate.now()));
        applyDao.save(apply);
        sendNotificationIfStatusChanged(apply);

        if (status == Status.APPROVED) {
            List<Apply> pendingApplies = applyDao.findAllByFarmerIdAndStatus(apply.getFarmerId(), Status.PENDING);

            pendingApplies.forEach(apply1 -> {
                apply1.setStatus(Status.DEACTIVATED);
                apply1.setStatusDate(String.valueOf(LocalDate.now()));
                apply1.setReview("Your other loan has already been approved");
                applyDao.save(apply1);
            });
        }
    }

    private void sendNotificationIfStatusChanged(Apply apply) {
        log.info("sendNotificationIfStatusChanged called for apply id: {} ", apply.getId());

        Farmer farmer = this.farmerDao.findById(apply.getFarmerId()).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.USER, UsersConstants.ID, apply.getFarmerId()));
        Scheme scheme = this.schemeDao.findById(apply.getSchemeId()).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.SCHEME, UsersConstants.ID, String.valueOf(apply.getSchemeId())));
        Bank bank = this.bankDao.findById(apply.getBankId()).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.BANK, UsersConstants.ID, apply.getBankId()));

        String shortMsg = "Your application for " + scheme.getSchemeName() + " is now " + apply.getStatus();

        // Detailed message for full view
        String fullMsg = "Hello " + farmer.getFirstName() + " " + farmer.getLastName() + ",<br><br>"
                + "Your application for the scheme <strong>" + scheme.getSchemeName() + "</strong> "
                + "with bank <strong>" + bank.getBankName() + "</strong> has been updated to status: "
                + "<strong>" + apply.getStatus() + "</strong>.<br><br>"
                + "Review message: " + apply.getReview();

        // Save in-app notification
        log.info("Save in-app notification");
        notificationService.createNotification(farmer, shortMsg, fullMsg);

        //Send email
        //emailService.sendEmail("Status Update: " + scheme.getSchemeName(), fullMsg, farmer.getEmail());
    }

    @Override
    public ApplyDTO getApply(Integer id) {
        Apply apply = applyDao.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.APPLY, UsersConstants.ID, String.valueOf(id)));
        return this.modelMapper.map(apply, ApplyDTO.class);
    }

    @Override
    public List<GrievencesDTO> getGrievences(String bankId) {
        List<Grievences> grievences = this.grievencesDao.findAllByBankId(bankId);
        return grievences.stream().map(grievence -> {
            GrievencesDTO grievencesDTO = this.modelMapper.map(grievence, GrievencesDTO.class);
            Farmer farmer = this.farmerDao.findById(grievence.getFarmerId()).orElseThrow(() ->
                    new ResourceNotFoundException(UsersConstants.FARMER, UsersConstants.ID, grievence.getFarmerId()));
            Bank bank = this.bankDao.findById(grievence.getBankId()).orElseThrow(() ->
                    new ResourceNotFoundException(UsersConstants.BANK, UsersConstants.ID, grievence.getBankId()));
            grievencesDTO.setFarmerDTO(this.modelMapper.map(farmer, FarmerDTO.class));
            grievencesDTO.setBankDTO(this.modelMapper.map(bank, BankDTO.class));

            return grievencesDTO;
        }).collect(Collectors.toList());
    }
}
