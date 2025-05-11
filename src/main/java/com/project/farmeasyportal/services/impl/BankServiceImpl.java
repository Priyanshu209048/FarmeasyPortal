package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.dao.*;
import com.project.farmeasyportal.entities.*;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.*;
import com.project.farmeasyportal.services.BankService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final SchemeDao schemeDao;
    private final BankDao bankDao;
    private final ApplyDao applyDao;
    private final UserDao userDao;
    private final FarmerDao farmerDao;
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
        user.setRole("ROLE_BANK");
        userDao.save(user);
        Bank save = this.bankDao.save(bank);
        return this.modelMapper.map(save, BankDTO.class);
    }

    @Override
    public BankDTO getBankById(String bankId) {
        Bank bank = this.bankDao.findById(bankId).orElseThrow(() ->
                new ResourceNotFoundException("Bank", "id", bankId));
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
                new ResourceNotFoundException("Bank", "id", bankId));

        scheme.setBankId(bank.getId());
        Scheme save = schemeDao.save(scheme);
        return this.modelMapper.map(save, SchemeDTO.class);
    }

    @Override
    public SchemeDTO updateScheme(SchemeDTO schemeDTO, Integer schemeId) {
        Scheme scheme = this.schemeDao.findById(schemeId).orElseThrow(() ->
                new ResourceNotFoundException("Scheme", "id", String.valueOf(schemeId)));

        scheme.setSchemeName(schemeDTO.getSchemeName());
        scheme.setSchemeCode(schemeDTO.getSchemeCode());
        scheme.setSchemeDescription(schemeDTO.getSchemeDescription());
        scheme.setBenefits(schemeDTO.getBenefits());
        scheme.setEligibility(schemeDTO.getEligibility());
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
                    new ResourceNotFoundException("Bank", "id", scheme.getBankId()));
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
                    new ResourceNotFoundException("Bank", "id", scheme.getBankId()));
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
        List<Apply> applyList = this.applyDao.findAllByBankId(bankId);
        return applyList.stream().map(apply -> {
            ApplyDTO applyDTO = this.modelMapper.map(apply, ApplyDTO.class);
            Farmer farmer = this.farmerDao.findById(apply.getFarmerId()).orElseThrow(() ->
                    new ResourceNotFoundException("Farmer", "id", apply.getFarmerId()));
            Bank bank = this.bankDao.findById(bankId).orElseThrow(() ->
                    new ResourceNotFoundException("Bank", "id", bankId));
            Scheme scheme = this.schemeDao.findById(Integer.valueOf(apply.getSchemeId())).orElseThrow(() ->
                    new ResourceNotFoundException("Scheme", "id", apply.getSchemeId()));

            applyDTO.setFarmerDTO(this.modelMapper.map(farmer, FarmerDTO.class));
            applyDTO.setBankDTO(this.modelMapper.map(bank, BankDTO.class));
            applyDTO.setSchemeDTO(this.modelMapper.map(scheme, SchemeDTO.class));
            return applyDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateApply(Integer applyId, ApplyUpdateDTO applyUpdateDTO) {
        Apply apply = this.applyDao.findById(applyId).orElseThrow(() ->
                new ResourceNotFoundException("Apply", "id", String.valueOf(applyId)));
        apply.setStatus(applyUpdateDTO.getStatus());
        apply.setReview(applyUpdateDTO.getReview());
        apply.setStatusDate(String.valueOf(LocalDate.now()));
        applyDao.save(apply);
    }

    @Override
    public ApplyDTO getApply(Integer id) {
        Apply apply = applyDao.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Apply", "id", String.valueOf(id)));
        return this.modelMapper.map(apply, ApplyDTO.class);
    }
}
