package com.project.farmeasyportal.services;

import com.project.farmeasyportal.entities.Farmer;
import com.project.farmeasyportal.entities.Grievences;
import com.project.farmeasyportal.entities.LoanForm;
import com.project.farmeasyportal.payloads.*;
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

    void submitForm(LoanFormDTO loanFormDTO, MultipartFile file, String originalFileName, String userId) throws IOException;
    Boolean isUserSubmittedForm(String email);
    LoanFormDTO getLoanFormByEmail(String email);
    LoanFormDTO updateLoanForm(LoanFormDTO loanFormDTO, MultipartFile file, String originalFileName, String userId) throws IOException;

    ApplyDTO applyLoanScheme(Integer schemeId, String farmerId, String amount);
    List<ApplyDTO> getApplyStatus(String farmerId);

    void addGrievence(GrievencesRequestDTO grievencesRequestDTO, FarmerDTO farmerDTO);

}
