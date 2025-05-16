package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.constants.UsersConstants;
import com.project.farmeasyportal.dao.GrievencesDao;
import com.project.farmeasyportal.entities.Grievences;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
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
    private final ModelMapper modelMapper;

    @Override
    public List<GrievencesDTO> getALlGrievences() {
        List<Grievences> grievences = this.grievencesDao.findAll();
        return grievences.stream().map(grievence -> this.modelMapper.map(grievence, GrievencesDTO.class)).collect(Collectors.toList());
    }

    @Override
    public GrievencesDTO updateGrievences(GrievencesResponseDTO grievencesResponseDTO, Integer grievenceId) {
        Grievences grievences = this.grievencesDao.findById(grievenceId).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.GRIEVENCE, UsersConstants.ID, String.valueOf(grievenceId)));

        grievences.setGrievencesStatus(grievencesResponseDTO.getStatus());
        grievences.setGrievencesReview(grievencesResponseDTO.getReview());
        Grievences update = this.grievencesDao.save(grievences);

        return this.modelMapper.map(update, GrievencesDTO.class);
    }
}
