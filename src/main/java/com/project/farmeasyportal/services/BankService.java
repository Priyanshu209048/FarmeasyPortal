package com.project.farmeasyportal.services;

import com.project.farmeasyportal.payloads.*;

import java.util.List;

public interface BankService {

    BankDTO addBank(BankDTO bankDTO);
    BankDTO getBankById(String bankId);
    BankDTO getBankByEmail(String email);
    List<BankDTO> getBanks();
    Boolean isBankExistById(String bankId);
    Boolean isBankExistByEmail(String email);

    SchemeDTO addScheme(SchemeDTO schemeDTO, String bankId);
    SchemeDTO updateScheme(SchemeDTO schemeDTO, Integer schemeId);
    List<SchemeDTO> getSchemesByBank(String username);
    List<SchemeDTO> getSchemes();
    SchemeDTO getSchemeById(Integer schemeId);
    void deleteScheme(Integer schemeId);

    List<ApplyDTO> getApplies();
    List<ApplyDTO> getApplyByBank(String bankId);
    void updateApply(Integer applyId, ApplyUpdateDTO applyUpdateDTO);
    ApplyDTO getApply(Integer id);

    List<GrievencesDTO> getGrievences(String bankId);

}
