package com.project.farmeasyportal.controllers;

import com.project.farmeasyportal.dao.BankDao;
import com.project.farmeasyportal.dao.GrievencesDao;
import com.project.farmeasyportal.entities.Bank;
import com.project.farmeasyportal.entities.Scheme;
import com.project.farmeasyportal.payloads.SchemeDTO;
import com.project.farmeasyportal.services.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank")
public class BankController {

    private final BankService bankService;
    private final BankDao bankDao;
    private final GrievencesDao grievencesDao;

    @PostMapping("/add-scheme")
    public ResponseEntity<?> addScheme(@RequestBody @Valid SchemeDTO schemeDTO, Authentication authentication) {
        String username = authentication.getName();
        Bank bank = this.bankDao.findByEmail(username);
        Scheme scheme = this.bankService.addScheme(schemeDTO, bank.getId());
        return new ResponseEntity<>(scheme, HttpStatus.OK);
    }

}
