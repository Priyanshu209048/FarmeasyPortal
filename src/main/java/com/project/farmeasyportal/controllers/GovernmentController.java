package com.project.farmeasyportal.controllers;

import com.project.farmeasyportal.payloads.GrievencesDTO;
import com.project.farmeasyportal.payloads.GrievencesResponseDTO;
import com.project.farmeasyportal.services.GovService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gov")
public class GovernmentController {

    private final GovService govService;

    @GetMapping("/grievences")
    public ResponseEntity<?> getAllGrievences() {
        List<GrievencesDTO> grievences = this.govService.getALlGrievences();
        return new ResponseEntity<>(grievences, HttpStatus.OK);
    }

    @PutMapping("/update-grievences/{grievenceId}")
    public ResponseEntity<?> updateGrievence(@PathVariable Integer grievenceId, @RequestBody GrievencesResponseDTO grievencesResponseDTO) {
        GrievencesDTO grievencesDTO = this.govService.updateGrievences(grievencesResponseDTO, grievenceId);
        return new ResponseEntity<>(grievencesDTO, HttpStatus.OK);
    }


}
