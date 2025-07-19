package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.constants.UsersConstants;
import com.project.farmeasyportal.dao.BankDao;
import com.project.farmeasyportal.dao.FarmerDao;
import com.project.farmeasyportal.dao.GrievencesDao;
import com.project.farmeasyportal.entities.Bank;
import com.project.farmeasyportal.entities.Farmer;
import com.project.farmeasyportal.entities.Grievences;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.BankDTO;
import com.project.farmeasyportal.payloads.FarmerDTO;
import com.project.farmeasyportal.payloads.GrievencesDTO;
import com.project.farmeasyportal.payloads.GrievencesResponseDTO;
import com.project.farmeasyportal.services.GovService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GovServiceImpl implements GovService {

    private final GrievencesDao grievencesDao;
    private final FarmerDao farmerDao;
    private final BankDao bankDao;
    private final ModelMapper modelMapper;

    @Override
    public List<GrievencesDTO> getALlGrievences() {
        List<Grievences> grievences = this.grievencesDao.findAll();
        return grievences.stream().map(grievence -> {
            GrievencesDTO grievencesDTO = this.modelMapper.map(grievence, GrievencesDTO.class);
            Farmer farmer = this.farmerDao.findById(grievence.getFarmerId()).orElseThrow(() ->
                    new ResourceNotFoundException(UsersConstants.FARMER, UsersConstants.ID, grievence.getFarmerId()));

            Bank bank = this.bankDao.findById(grievence.getBankId()).orElseThrow(() ->
                    new ResourceNotFoundException(UsersConstants.BANK, UsersConstants.ID, grievence.getBankId()));

            grievencesDTO.setFarmerDTO(this.modelMapper.map(farmer, FarmerDTO.class));
            grievencesDTO.setBankDTO(this.modelMapper.map(bank, BankDTO.class));

            return grievencesDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public GrievencesDTO updateGrievences(GrievencesResponseDTO grievencesResponseDTO, Integer grievenceId) {
        Grievences grievences = this.grievencesDao.findById(grievenceId).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.GRIEVENCE, UsersConstants.ID, String.valueOf(grievenceId)));

        grievences.setGrievencesStatus(grievencesResponseDTO.getStatus());
        grievences.setGrievencesReview(grievencesResponseDTO.getReview());
        System.out.println("Updating grievance #" + grievenceId +
                " with status: " + grievencesResponseDTO.getStatus() +
                ", review: " + grievencesResponseDTO.getReview());

        Grievences update = this.grievencesDao.save(grievences);

        return this.modelMapper.map(update, GrievencesDTO.class);
    }
}
