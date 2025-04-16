package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.dao.FarmerDao;
import com.project.farmeasyportal.dao.UserDao;
import com.project.farmeasyportal.entities.Farmer;
import com.project.farmeasyportal.entities.Grievences;
import com.project.farmeasyportal.entities.LoanForm;
import com.project.farmeasyportal.entities.User;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.FarmerDTO;
import com.project.farmeasyportal.services.FarmerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FarmerServiceImpl implements FarmerService {

    private final FarmerDao farmerDao;
    private final UserDao userDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

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
        Farmer farmer = this.farmerDao.findById(email).orElseThrow(() -> new ResourceNotFoundException("Farmer", "email", email));
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
    public void submitForm(LoanForm loanForm, MultipartFile file, String fileName, int userId) throws IOException {

    }

    @Override
    public Boolean isUserSubmittedForm(String email) {
        return null;
    }

    @Override
    public LoanForm getLoanFormByEmail(String email) {
        return null;
    }

    @Override
    public LoanForm updateLoanForm(LoanForm loanForm) {
        return null;
    }

    @Override
    public void addGrievence(Grievences grievence, Farmer farmer) {

    }
}
