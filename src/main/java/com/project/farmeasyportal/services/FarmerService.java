package com.project.farmeasyportal.services;

import com.project.farmeasyportal.entities.Farmer;
import com.project.farmeasyportal.entities.Grievences;
import com.project.farmeasyportal.entities.LoanForm;
import com.project.farmeasyportal.payloads.FarmerDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FarmerService {

    FarmerDTO saveFarmer(FarmerDTO farmerDTO);
    FarmerDTO updateFarmer(String id, FarmerDTO farmerDTO);
    FarmerDTO getFarmerByEmail(String id);
    FarmerDTO getFarmerById(String id);
    List<FarmerDTO> getAllFarmers();
    void deleteFarmer(String id);
    Boolean isFarmerExistByEmail(String id);
    Boolean isFarmerExistById(String id);

    void submitForm(LoanForm loanForm, MultipartFile file, String fileName, int userId) throws IOException;
    Boolean isUserSubmittedForm(String email);
    LoanForm getLoanFormByEmail(String email);
    LoanForm updateLoanForm(LoanForm loanForm);

    void addGrievence(Grievences grievence, Farmer farmer);

}
