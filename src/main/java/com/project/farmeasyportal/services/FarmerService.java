package com.project.farmeasyportal.services;

import com.project.farmeasyportal.entities.Farmer;
import com.project.farmeasyportal.entities.Grievences;
import com.project.farmeasyportal.entities.LoanForm;
import com.project.farmeasyportal.payloads.FarmerDTO;
import com.project.farmeasyportal.payloads.GrievencesDTO;
import com.project.farmeasyportal.payloads.LoanFormDTO;
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

    void submitForm(LoanFormDTO loanFormDTO, MultipartFile file, String userId) throws IOException;
    Boolean isUserSubmittedForm(String email);
    LoanFormDTO getLoanFormByEmail(String email);
    LoanFormDTO updateLoanForm(LoanFormDTO loanFormDTO);

    void addGrievence(GrievencesDTO grievencesDTO, FarmerDTO farmerDTO);

}
