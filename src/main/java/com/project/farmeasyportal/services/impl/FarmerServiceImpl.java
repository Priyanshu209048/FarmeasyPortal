package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.dao.*;
import com.project.farmeasyportal.entities.*;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.ApplyDTO;
import com.project.farmeasyportal.payloads.FarmerDTO;
import com.project.farmeasyportal.payloads.GrievencesDTO;
import com.project.farmeasyportal.payloads.LoanFormDTO;
import com.project.farmeasyportal.services.FarmerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FarmerServiceImpl implements FarmerService {

    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/documents";

    private final FarmerDao farmerDao;
    private final UserDao userDao;
    private final LoanFormDao loanFormDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final GrievencesDao grievencesDao;
    private final ApplyDao applyDao;
    private final BankDao bankDao;
    private final SchemeDao schemeDao;

    @Override
    public FarmerDTO saveFarmer(FarmerDTO farmerDTO) {
        if (farmerDTO.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(farmerDTO.getPassword());

        farmerDTO.setPassword(encodedPassword);
        farmerDTO.setId(String.valueOf(UUID.randomUUID()));
        Farmer farmer = modelMapper.map(farmerDTO, Farmer.class);

        User user = new User();
        user.setEmail(farmer.getEmail());
        user.setPassword(encodedPassword);
        user.setRole("ROLE_FARMER");
        userDao.save(user);
        Farmer save = farmerDao.save(farmer);
        return this.modelMapper.map(save, FarmerDTO.class);
    }

    @Override
    public FarmerDTO updateFarmer(String id, FarmerDTO farmerDTO) {
        Farmer farmer = this.farmerDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Farmer", "id", id));

        farmer.setFirstName(farmerDTO.getFirstName());
        farmer.setLastName(farmerDTO.getLastName());
        farmer.setContact(farmerDTO.getContact());
        Farmer update = farmerDao.save(farmer);

        return this.modelMapper.map(update, FarmerDTO.class);
    }

    @Override
    public FarmerDTO getFarmerByEmail(String email) {
        Farmer farmer = this.farmerDao.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Farmer", "email", email));
        return this.modelMapper.map(farmer, FarmerDTO.class);
    }

    @Override
    public FarmerDTO getFarmerById(String id) {
        Farmer farmer = this.farmerDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Farmer", "id", id));
        return this.modelMapper.map(farmer, FarmerDTO.class);
    }

    @Override
    public List<FarmerDTO> getAllFarmers() {
        List<Farmer> farmers = this.farmerDao.findAll();
        return farmers.stream().map(farmer -> this.modelMapper.map(farmer, FarmerDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteFarmer(String id) {
        Farmer farmer = this.farmerDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Farmer", "id", id));
        this.userDao.delete(this.userDao.findByEmail(farmer.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", farmer.getEmail())));
        this.farmerDao.delete(farmer);
    }

    @Override
    public Boolean isFarmerExistByEmail(String email) {
        return this.farmerDao.existsByEmail(email);
    }

    @Override
    public Boolean isFarmerExistById(String id) {
        return this.farmerDao.existsById(id);
    }

    @Override
    public void submitForm(LoanFormDTO loanFormDTO, MultipartFile file, String originalFileName, String userId) throws IOException {
        LoanForm loanForm = this.modelMapper.map(loanFormDTO, LoanForm.class);

        String storedPdfName;

        if (!file.isEmpty()) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            storedPdfName = userId + "_" + timestamp + ".pdf";

            Path directoryPath = Paths.get(uploadDir);
            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = directoryPath.resolve(storedPdfName);
            Files.write(filePath, file.getBytes());
        } else {
            storedPdfName = originalFileName;
        }

        loanForm.setPdfName(storedPdfName);

        loanFormDao.save(loanForm);
    }

    @Override
    public Boolean isUserSubmittedForm(String email) {
        return loanFormDao.existsByEmail(email);
    }

    @Override
    public LoanFormDTO getLoanFormByEmail(String email) {
        LoanForm loanForm = loanFormDao.findByEmail(email);
        return this.modelMapper.map(loanForm, LoanFormDTO.class);
    }

    @Override
    public LoanFormDTO updateLoanForm(LoanFormDTO loanFormDTO, MultipartFile file, String originalFileName, String userId) throws IOException {
        LoanForm existingLoanForm = this.loanFormDao.findById(loanFormDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Loan Form", "id", String.valueOf(loanFormDTO.getId())));
        String name = existingLoanForm.getName();
        String email = existingLoanForm.getEmail();
        String contact = existingLoanForm.getContact();
        String gender = existingLoanForm.getGender();

        LoanForm loanForm = this.modelMapper.map(loanFormDTO, LoanForm.class);

        loanForm.setId(existingLoanForm.getId());
        loanForm.setName(name);
        loanForm.setEmail(email);
        loanForm.setContact(contact);
        loanForm.setGender(gender);

        String storedPdfName;

        if (!file.isEmpty()) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            storedPdfName = userId + "_" + timestamp + ".pdf";

            Path directoryPath = Paths.get(uploadDir);
            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = directoryPath.resolve(storedPdfName);
            Files.write(filePath, file.getBytes());
        } else {
            storedPdfName = originalFileName;
        }

        loanForm.setPdfName(storedPdfName);
        loanFormDao.save(loanForm);

        return modelMapper.map(loanForm, LoanFormDTO.class);
    }

    @Override
    public ApplyDTO applyLoanScheme(Integer schemeId, String farmerId, String amount) {
        Scheme scheme = this.schemeDao.findById(schemeId).orElseThrow(() -> new ResourceNotFoundException("Scheme ", "id", String.valueOf(schemeId)));

        Apply apply = new Apply();
        apply.setFarmerId(farmerId);
        apply.setSchemeId(String.valueOf(schemeId));
        apply.setBankId(scheme.getBankId());
        apply.setDate(LocalDate.now());
        apply.setAmount(amount);
        apply.setReview("-");
        apply.setStatusDate("-");
        apply.setStatus("Pending");
        Apply save = this.applyDao.save(apply);
        return this.modelMapper.map(save, ApplyDTO.class);
    }

    @Override
    public void addGrievence(GrievencesDTO grievencesDTO, FarmerDTO farmerDTO) {
        Grievences grievence = this.modelMapper.map(grievencesDTO, Grievences.class);
        Farmer farmer = this.modelMapper.map(farmerDTO, Farmer.class);

        grievence.setFarmerId(farmer.getId());
        grievence.setGrievencesDate(LocalDate.now());
        grievence.setGrievencesReview("-");
        grievence.setGrievencesStatus("-");

        this.grievencesDao.save(grievence);
    }
}
