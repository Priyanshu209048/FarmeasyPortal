package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.constants.UsersConstants;
import com.project.farmeasyportal.dao.MerchantDao;
import com.project.farmeasyportal.dao.UserDao;
import com.project.farmeasyportal.entities.Merchant;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.MerchantDTO;
import com.project.farmeasyportal.services.MerchantService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final UserDao userDao;
    private final MerchantDao merchantDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public MerchantDTO saveMerchant(MerchantDTO MerchantDTO) {
        return null;
    }

    @Override
    public MerchantDTO updateMerchant(String id, MerchantDTO MerchantDTO) {
        return null;
    }

    @Override
    public MerchantDTO getMerchantByEmail(String id) {
        return null;
    }

    @Override
    public MerchantDTO getMerchantById(String id) {
        return null;
    }

    @Override
    public List<MerchantDTO> getAllMerchants() {
        return List.of();
    }

    @Override
    public void deleteMerchant(String id) {
        Merchant merchant = this.merchantDao.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.ID, id));

    }

    @Override
    public Boolean isMerchantExistByEmail(String email) {
        return this.merchantDao.existsByEmail(email);
    }

    @Override
    public Boolean isMerchantExistById(String id) {
        return this.merchantDao.existsById(id);
    }

}
