package com.project.farmeasyportal.services;

import com.project.farmeasyportal.payloads.ItemBookingDTO;
import com.project.farmeasyportal.payloads.ItemDTO;
import com.project.farmeasyportal.payloads.MerchantDTO;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MerchantService {

    MerchantDTO saveMerchant(MerchantDTO merchantDTO);
    MerchantDTO updateMerchant(String id, MerchantDTO merchantDTO);
    MerchantDTO getMerchantByEmail(String email);
    MerchantDTO getMerchantById(String id);
    List<MerchantDTO> getAllMerchants();
    void deleteMerchant(String id);
    Boolean isMerchantExistByEmail(String email);
    Boolean isMerchantExistById(String id);

    ItemDTO addItem(ItemDTO itemDTO, MultipartFile imageName, String merchantId) throws IOException;
    ItemDTO updateItem(ItemDTO itemDTO, Integer itemId);
    ItemDTO getItemById(Integer id);
    void deleteItemById(Integer id);
    List<ItemDTO> getItemsByMerchant(String username);
    List<ItemDTO> getItems();
    List<ItemBookingDTO> getAllOrdersByMerchantId(String merchantId);

}
