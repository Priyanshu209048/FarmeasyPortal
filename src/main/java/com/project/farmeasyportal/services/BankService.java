package com.project.farmeasyportal.services;

import com.project.farmeasyportal.entities.Scheme;
import com.project.farmeasyportal.payloads.ApplyDTO;
import com.project.farmeasyportal.payloads.BankDTO;
import com.project.farmeasyportal.payloads.FarmerDTO;
import com.project.farmeasyportal.payloads.SchemeDTO;

import java.util.List;

public interface BankService {

    BankDTO addBank(BankDTO bankDTO);
    BankDTO getBankById(String bankId);
    List<BankDTO> getBanks();
    Boolean isBankExistById(String bankId);
    Boolean isBankExistByEmail(String email);

    SchemeDTO addScheme(SchemeDTO schemeDTO, String bankId);
    SchemeDTO updateScheme(SchemeDTO schemeDTO, Integer schemeId);
    List<SchemeDTO> getSchemesByBank(String username);
    List<SchemeDTO> getSchemes();

    List<ApplyDTO> getApplies();
    List<ApplyDTO> getApplyByBank(String username);
    List<ApplyDTO> getApplyByFarmer(FarmerDTO farmerDTO);
    void updateApply(ApplyDTO applyDTO, String status, String review);
    ApplyDTO getApply(Integer id);

}
