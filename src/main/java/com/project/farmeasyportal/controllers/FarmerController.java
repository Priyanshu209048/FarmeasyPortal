package com.project.farmeasyportal.controllers;

import com.project.farmeasyportal.payloads.ApiResponse;
import com.project.farmeasyportal.payloads.FarmerDTO;
import com.project.farmeasyportal.services.FarmerService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFarmer(@PathVariable("id") String farmerID, @RequestBody @Valid FarmerDTO farmerDTO) {
        if (!this.farmerService.isFarmerExistById(farmerID)) {
            log.error("User does not exist");
            return new ResponseEntity<>(new ApiResponse("User does not exist"), HttpStatus.NOT_FOUND);
        }
        FarmerDTO updateFarmerDTO = farmerService.updateFarmer(farmerID, farmerDTO);
        return new ResponseEntity<>(new ApiResponse("User Updated Successfully!!"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFarmerById(@PathVariable("id") String farmerID) {
        if (!this.farmerService.isFarmerExistById(farmerID)) {
            return new ResponseEntity<>(new ApiResponse("User does not exist"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.farmerService.getFarmerById(farmerID), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllFarmers() {
        return new ResponseEntity<>(this.farmerService.getAllFarmers(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFarmer(@PathVariable("id") String farmerID) {
        if (!this.farmerService.isFarmerExistById(farmerID)) {
            return new ResponseEntity<>(new ApiResponse("User does not exist"), HttpStatus.NOT_FOUND);
        }
        this.farmerService.deleteFarmer(farmerID);
        return new ResponseEntity<>(new ApiResponse("User Deleted Successfully!!"), HttpStatus.OK);
    }

}
