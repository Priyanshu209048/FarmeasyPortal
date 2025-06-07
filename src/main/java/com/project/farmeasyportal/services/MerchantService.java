package com.project.farmeasyportal.services;

import com.project.farmeasyportal.payloads.MerchantDTO;

import java.util.List;

public interface MerchantService {

    MerchantDTO saveMerchant(MerchantDTO MerchantDTO);
    MerchantDTO updateMerchant(String id, MerchantDTO MerchantDTO);
    MerchantDTO getMerchantByEmail(String id);
    MerchantDTO getMerchantById(String id);
    List<MerchantDTO> getAllMerchants();
    void deleteMerchant(String id);
    Boolean isMerchantExistByEmail(String email);
    Boolean isMerchantExistById(String id);
    
}
