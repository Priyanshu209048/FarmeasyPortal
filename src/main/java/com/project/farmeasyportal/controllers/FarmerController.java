package com.project.farmeasyportal.controllers;

import com.project.farmeasyportal.entities.Farmer;
import com.project.farmeasyportal.payloads.ApiResponse;
import com.project.farmeasyportal.payloads.FarmerDTO;
import com.project.farmeasyportal.payloads.LoanFormDTO;
import com.project.farmeasyportal.services.FarmerService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/farmer")
@Log4j2
public class FarmerController {

    private final FarmerService farmerService;
    /*private static final Logger log = LoggerFactory.getLogger(FarmerController.class);*/

    @Autowired
    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    private ResponseEntity<?> checkFarmerExists(String farmerID) {
        if (!this.farmerService.isFarmerExistById(farmerID)) {
            log.error("User does not exist");
            return new ResponseEntity<>(new ApiResponse("User does not exist"), HttpStatus.NOT_FOUND);
        }
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFarmer(@PathVariable("id") String farmerID, @RequestBody @Valid FarmerDTO farmerDTO) {
        ResponseEntity<?> existenceCheck = checkFarmerExists(farmerID);
        if (existenceCheck != null)
            return existenceCheck;

        FarmerDTO updateFarmerDTO = this.farmerService.updateFarmer(farmerID, farmerDTO);
        log.info("User updated successfully : {}", updateFarmerDTO);
        return new ResponseEntity<>(updateFarmerDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFarmerById(@PathVariable("id") String farmerID) {
        ResponseEntity<?> existenceCheck = checkFarmerExists(farmerID);
        if (existenceCheck != null)
            return existenceCheck;

        FarmerDTO farmer = this.farmerService.getFarmerById(farmerID);
        log.info("User found successfully : {}", farmer);
        return new ResponseEntity<>(farmer, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllFarmers() {
        List<FarmerDTO> farmers = this.farmerService.getAllFarmers();
        log.info("Get all farmers : {}", farmers);
        return new ResponseEntity<>(farmers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFarmer(@PathVariable("id") String farmerID) {
        ResponseEntity<?> existenceCheck = checkFarmerExists(farmerID);
        if (existenceCheck != null)
            return existenceCheck;
        
        this.farmerService.deleteFarmer(farmerID);
        log.info("User deleted successfully : {}", farmerID);
        return new ResponseEntity<>(new ApiResponse("User Deleted Successfully!!"), HttpStatus.OK);
    }

    @PostMapping(value = "/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> loanFormProcess(@RequestBody @Valid LoanFormDTO loanFormDTO,
                                             @RequestPart("documents") MultipartFile file,
                                             Authentication authentication) throws IOException {

        // Ensure the authenticated user is the same as the loan form's email
        if (!loanFormDTO.getEmail().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access.");
        }

        FarmerDTO farmerDTO = this.farmerService.getFarmerByEmail(authentication.getName());
        this.farmerService.submitForm(loanFormDTO, file, farmerDTO.getId());

        return ResponseEntity.ok("Loan form submitted successfully.");
    }

}
