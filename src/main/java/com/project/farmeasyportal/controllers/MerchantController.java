package com.project.farmeasyportal.controllers;

import com.project.farmeasyportal.constants.UsersConstants;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.ItemDTO;
import com.project.farmeasyportal.payloads.MerchantDTO;
import com.project.farmeasyportal.services.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/merchant")
public class MerchantController {

    private final MerchantService merchantService;

    @PutMapping("/update")
    public ResponseEntity<?> updateMerchant(@Valid @RequestBody MerchantDTO merchantDTO, Authentication authentication) {
        String username = authentication.getName();
        MerchantDTO merchant = this.merchantService.getMerchantByEmail(username);
        if (merchant == null) {
            return new ResponseEntity<>(new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.EMAIL, username), HttpStatus.NOT_FOUND);
        }

        MerchantDTO updated = this.merchantService.updateMerchant(merchant.getId(), merchantDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{merchantId}")
    public ResponseEntity<?> getMerchantById(@PathVariable String merchantId) {
        if (!this.merchantService.isMerchantExistById(merchantId)) {
            return new ResponseEntity<>("Merchant doesn't exists !!", HttpStatus.NOT_FOUND);
        }

        MerchantDTO merchant = this.merchantService.getMerchantById(merchantId);
        return new ResponseEntity<>(merchant, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllMerchants() {
        return new ResponseEntity<>(this.merchantService.getAllMerchants(), HttpStatus.OK);
    }

    @PostMapping("/add-item")
    public ResponseEntity<?> addItem(@RequestBody @Valid ItemDTO itemDTO, Authentication authentication) {
        String username = authentication.getName();
        MerchantDTO merchant = this.merchantService.getMerchantByEmail(username);
        if (merchant == null) {
            return new ResponseEntity<>(new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.EMAIL, username), HttpStatus.NOT_FOUND);
        }

        ItemDTO item = this.merchantService.addItem(itemDTO, merchant.getId());
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PutMapping("/update-item/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable Integer itemId, @RequestBody @Valid ItemDTO itemDTO, Authentication authentication) {
        String username = authentication.getName();
        MerchantDTO merchant = this.merchantService.getMerchantByEmail(username);
        if (merchant == null) {
            return new ResponseEntity<>(new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.EMAIL, username), HttpStatus.NOT_FOUND);
        }

        ItemDTO item = this.merchantService.updateItem(itemDTO, itemId);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping("/items-merchant")
    public ResponseEntity<?> getAllItemsByMerchant(Authentication authentication) {
        String username = authentication.getName();
        MerchantDTO merchant = this.merchantService.getMerchantByEmail(username);
        if (merchant == null) {
            return new ResponseEntity<>(new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.EMAIL, username), HttpStatus.NOT_FOUND);
        }

        List<ItemDTO> items = this.merchantService.getItemsByMerchant(username);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

}
