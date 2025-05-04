package com.project.farmeasyportal.services;

import com.project.farmeasyportal.entities.Scheme;
import com.project.farmeasyportal.payloads.ApplyDTO;
import com.project.farmeasyportal.payloads.BankDTO;
import com.project.farmeasyportal.payloads.FarmerDTO;
import com.project.farmeasyportal.payloads.SchemeDTO;

import java.util.List;

public interface BankService {

    //void addBank(String email, String password, String role, int bankId);
    Scheme addScheme(SchemeDTO schemeDTO, String bankId);
    List<BankDTO> getBanks();

    List<SchemeDTO> getSchemes(String username);
    public List<SchemeDTO> getSchemes();

    List<ApplyDTO> getApplies();
    List<ApplyDTO> getApplyByBank(String username);
    List<ApplyDTO> getApplyByFarmer(FarmerDTO farmerDTO);
    void updateApply(ApplyDTO applyDTO, String status, String review);
    ApplyDTO getApply(Integer id);

}
