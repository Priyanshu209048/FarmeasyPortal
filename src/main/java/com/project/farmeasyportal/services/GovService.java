package com.project.farmeasyportal.services;

import com.project.farmeasyportal.payloads.GrievencesDTO;
import com.project.farmeasyportal.payloads.GrievencesResponseDTO;

import java.util.List;

public interface GovService {

    List<GrievencesDTO> getALlGrievences();
    GrievencesDTO updateGrievences(GrievencesResponseDTO grievencesResponseDTO, Integer grievenceId);

}
