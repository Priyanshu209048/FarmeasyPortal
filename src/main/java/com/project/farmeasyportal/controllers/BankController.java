package com.project.farmeasyportal.controllers;

import com.project.farmeasyportal.constants.UsersConstants;
import com.project.farmeasyportal.dao.BankDao;
import com.project.farmeasyportal.dao.GrievencesDao;
import com.project.farmeasyportal.entities.Bank;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.*;
import com.project.farmeasyportal.services.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bank")
public class BankController {

    private static final Logger log = LoggerFactory.getLogger(BankController.class);
    private final BankService bankService;
    private final BankDao bankDao;
    private final GrievencesDao grievencesDao;

    @PostMapping("/api/v1/add-bank")
    public ResponseEntity<?> addBank(@Valid @RequestBody BankDTO bankDTO) {
        if (this.bankService.isBankExistByEmail(bankDTO.getEmail())) {
            return new ResponseEntity<>(new ApiResponse("Bank already exists !!"), HttpStatus.CONFLICT);
        }
        BankDTO bank = this.bankService.addBank(bankDTO);
        return new ResponseEntity<>(bank, HttpStatus.CREATED);
    }

    @GetMapping("/{bankId}")
    public ResponseEntity<?> getBankById(@PathVariable String bankId) {
        if (!this.bankService.isBankExistById(bankId)) {
            return new ResponseEntity<>(new ApiResponse("Bank doesn't exists !!"), HttpStatus.NOT_FOUND);
        }
        BankDTO bank = this.bankService.getBankById(bankId);
        return new ResponseEntity<>(bank, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllBanks() {
        List<BankDTO> banks = this.bankService.getBanks();
        return new ResponseEntity<>(banks, HttpStatus.OK);
    }

    @PostMapping("/add-scheme")
    public ResponseEntity<?> addScheme(@RequestBody @Valid SchemeDTO schemeDTO, Authentication authentication) {
        String username = authentication.getName();
        Bank bank = this.bankDao.findByEmail(username);
        if (bank == null) {
            throw new ResourceNotFoundException(UsersConstants.BANK, UsersConstants.EMAIL, username);
        }
        SchemeDTO scheme = this.bankService.addScheme(schemeDTO, bank.getId());
        return new ResponseEntity<>(scheme, HttpStatus.CREATED);
    }

    @PutMapping("/update-scheme/{schemeId}")
    public ResponseEntity<?> updateScheme(@PathVariable("schemeId") Integer schemeId, @RequestBody @Valid SchemeDTO schemeDTO, Authentication authentication) {
        /*String username = authentication.getName();
        Bank bank = this.bankDao.findByEmail(username);

        if (bank == null) {
            throw new ResourceNotFoundException("Bank", "email", username);
        }
        if (schemeDTO.getBankDTO() == null || !Objects.equals(bank.getId(), schemeDTO.getBankDTO().getId())) {
            return new ResponseEntity<>(new ApiResponse("Unauthorized bank access !!"), HttpStatus.UNAUTHORIZED);
        }*/

        SchemeDTO updatedScheme = this.bankService.updateScheme(schemeDTO, schemeId);
        return new ResponseEntity<>(updatedScheme, HttpStatus.OK);
    }

    @GetMapping("/scheme")
    public ResponseEntity<?> getAllSchemesByBank(Authentication authentication) {
        String username = authentication.getName();
        List<SchemeDTO> schemes = this.bankService.getSchemesByBank(username);
        return new ResponseEntity<>(schemes, HttpStatus.OK);
    }

    @GetMapping("/apply-status-farmer")
    public ResponseEntity<?> getAllApplyStatusFarmer(Authentication authentication) {
        String username = authentication.getName();
        BankDTO bankDTO = this.bankService.getBankByEmail(username);
        List<ApplyDTO> applyByBank = this.bankService.getApplyByBank(bankDTO.getId());
        return new ResponseEntity<>(applyByBank, HttpStatus.OK);
    }

    @PutMapping("/update-apply-status/{applyId}")
    public ResponseEntity<?> updateApplyStatus(@PathVariable Integer applyId, @RequestBody @Valid ApplyUpdateDTO applyUpdateDTO) {
        this.bankService.updateApply(applyId, applyUpdateDTO);
        return new ResponseEntity<>("Apply status updated successfully !!", HttpStatus.OK);
    }

    @GetMapping("/grievences")
    public ResponseEntity<?> getGrievences(Authentication authentication) {
        String name = authentication.getName();
        log.info("Bank Email: {}", name);
        BankDTO bank = this.bankService.getBankByEmail(name);
        List<GrievencesDTO> grievences = this.bankService.getGrievences(bank.getId());
        return new ResponseEntity<>(grievences, HttpStatus.OK);
    }

}
