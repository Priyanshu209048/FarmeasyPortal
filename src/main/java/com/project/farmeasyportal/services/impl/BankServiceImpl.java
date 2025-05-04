package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.dao.*;
import com.project.farmeasyportal.entities.Apply;
import com.project.farmeasyportal.entities.Bank;
import com.project.farmeasyportal.entities.Farmer;
import com.project.farmeasyportal.entities.Scheme;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.ApplyDTO;
import com.project.farmeasyportal.payloads.BankDTO;
import com.project.farmeasyportal.payloads.FarmerDTO;
import com.project.farmeasyportal.payloads.SchemeDTO;
import com.project.farmeasyportal.services.BankService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final SchemeDao schemeDao;
    private final BankDao bankDao;
    private final ApplyDao applyDao;
    private final ModelMapper modelMapper;
    private final GrievencesDao grievencesDao;

    @Override
    public Scheme addScheme(SchemeDTO schemeDTO, String bankId) {
        Scheme scheme = modelMapper.map(schemeDTO, Scheme.class);
        Bank bank = this.bankDao.findById(bankId).orElseThrow(() ->  new ResourceNotFoundException("Bank", "id", bankId));

        scheme.setBankId(bank.getId());
        Scheme save = schemeDao.save(scheme);
        return this.modelMapper.map(save, Scheme.class);
    }

    @Override
    public List<BankDTO> getBanks() {
        List<Bank> bankList = this.bankDao.findAll();
        return bankList.stream().map(bank -> this.modelMapper.map(bank, BankDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<SchemeDTO> getSchemes(String username) {
        List<Scheme> schemes = schemeDao.findAllByBankId(bankDao.findByEmail(username).getId());
        return schemes.stream().map(scheme -> this.modelMapper.map(scheme, SchemeDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<SchemeDTO> getSchemes() {
        return this.schemeDao.findAll().stream().map(scheme -> this.modelMapper.map(scheme, SchemeDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ApplyDTO> getApplies() {
        return applyDao.findAll().stream().map(apply -> this.modelMapper.map(apply, ApplyDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ApplyDTO> getApplyByBank(String username) {
        List<Apply> allByBank = applyDao.findAllByBankId(bankDao.findByEmail(username).getId());
        return allByBank.stream().map(apply -> this.modelMapper.map(apply, ApplyDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ApplyDTO> getApplyByFarmer(FarmerDTO farmerDTO) {
        Farmer farmer = modelMapper.map(farmerDTO, Farmer.class);
        List<Apply> applies = applyDao.findAllByFarmerId(farmer.getId());
        return applies.stream().map(apply -> this.modelMapper.map(apply, ApplyDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void updateApply(ApplyDTO applyDTO, String status, String review) {
        Apply apply = modelMapper.map(applyDTO, Apply.class);
        apply.setStatus(status);
        apply.setReview(review);
        apply.setStatusDate(String.valueOf(LocalDate.now()));
        applyDao.save(apply);
    }

    @Override
    public ApplyDTO getApply(Integer id) {
        Apply apply = applyDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Apply", "id", String.valueOf(id)));
        return this.modelMapper.map(apply, ApplyDTO.class);
    }
}
