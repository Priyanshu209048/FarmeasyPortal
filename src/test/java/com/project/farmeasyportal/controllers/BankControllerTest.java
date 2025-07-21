package com.project.farmeasyportal.controllers;

import com.project.farmeasyportal.constants.UsersConstants;
import com.project.farmeasyportal.entities.Bank;
import com.project.farmeasyportal.enums.Status;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.*;
import com.project.farmeasyportal.services.BankService;
import com.project.farmeasyportal.services.FarmerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankControllerTest {

    @Mock
    private BankService bankService;

    @Mock
    private FarmerService farmerService;

    @Mock
    private Authentication authentication;

    @Mock
    private com.project.farmeasyportal.dao.BankDao bankDao;

    @Mock
    private com.project.farmeasyportal.dao.SchemeDao schemeDao;

    @Mock
    private com.project.farmeasyportal.dao.ApplyDao applyDao;

    @Mock
    private com.project.farmeasyportal.dao.GrievencesDao grievencesDao;

    @InjectMocks
    private BankController bankController;

    private BankDTO bankDTO;
    private Bank bankEntity;

    @BeforeEach
    void setup() {
        bankDTO = new BankDTO();
        bankDTO.setId("bank-1");
        bankDTO.setEmail("bank@example.com");
        bankDTO.setBankName("Test Bank");

        bankEntity = new Bank();
        bankEntity.setId("bank-1");
        bankEntity.setEmail("bank@example.com");
        bankEntity.setBankName("Test Bank");
    }

    @Test
    void addBank_ShouldReturnConflict_WhenBankExists() {
        when(bankService.isBankExistByEmail(bankDTO.getEmail())).thenReturn(true);

        ResponseEntity<?> response = bankController.addBank(bankDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Bank already exists"));
        verify(bankService, never()).addBank(any());
    }

    @Test
    void addBank_ShouldCreateBank_WhenNotExists() {
        when(bankService.isBankExistByEmail(bankDTO.getEmail())).thenReturn(false);
        when(bankService.addBank(bankDTO)).thenReturn(bankDTO);

        ResponseEntity<?> response = bankController.addBank(bankDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bankDTO, response.getBody());
        verify(bankService).addBank(bankDTO);
    }

    @Test
    void getBankById_ShouldReturnNotFound_WhenBankDoesNotExist() {
        String bankId = "bank-1";
        when(bankService.isBankExistById(bankId)).thenReturn(false);

        ResponseEntity<?> response = bankController.getBankById(bankId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Bank doesn't exists"));
    }

    @Test
    void getBankById_ShouldReturnBankDTO_WhenBankExists() {
        String bankId = "bank-1";
        when(bankService.isBankExistById(bankId)).thenReturn(true);
        when(bankService.getBankById(bankId)).thenReturn(bankDTO);

        ResponseEntity<?> response = bankController.getBankById(bankId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bankDTO, response.getBody());
    }

    @Test
    void getAllBanks_ShouldReturnListOfBanks() {
        List<BankDTO> bankList = List.of(bankDTO);
        when(bankService.getBanks()).thenReturn(bankList);

        ResponseEntity<?> response = bankController.getAllBanks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bankList, response.getBody());
    }

    @Test
    void addScheme_ShouldReturnCreated_WhenBankExists() {
        SchemeDTO schemeDTO = new SchemeDTO();
        schemeDTO.setSchemeName("Test Scheme");

        when(authentication.getName()).thenReturn("bank@example.com");
        when(bankDao.findByEmail("bank@example.com")).thenReturn(bankEntity);
        when(bankService.addScheme(schemeDTO, bankEntity.getId())).thenReturn(schemeDTO);

        ResponseEntity<?> response = bankController.addScheme(schemeDTO, authentication);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(schemeDTO, response.getBody());
    }

    @Test
    void addScheme_ShouldThrowResourceNotFound_WhenBankNotFound() {
        SchemeDTO schemeDTO = new SchemeDTO();
        schemeDTO.setSchemeName("Test Scheme");

        when(authentication.getName()).thenReturn("bank@example.com");
        when(bankDao.findByEmail("bank@example.com")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> bankController.addScheme(schemeDTO, authentication));
    }

    @Test
    void updateScheme_ShouldReturnUpdatedScheme() {
        SchemeDTO schemeDTO = new SchemeDTO();
        schemeDTO.setSchemeName("Updated Scheme");
        Integer schemeId = 1;

        when(bankService.updateScheme(schemeDTO, schemeId)).thenReturn(schemeDTO);

        ResponseEntity<?> response = bankController.updateScheme(schemeId, schemeDTO, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(schemeDTO, response.getBody());
    }

    @Test
    void getSchemeById_ShouldReturnSchemeDTO() {
        SchemeDTO schemeDTO = new SchemeDTO();
        Integer schemeId = 1;

        when(bankService.getSchemeById(schemeId)).thenReturn(schemeDTO);

        ResponseEntity<SchemeDTO> response = bankController.getSchemeById(schemeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(schemeDTO, response.getBody());
    }

    @Test
    void getAllSchemesByBank_ShouldReturnListOfSchemes() {
        List<SchemeDTO> schemeList = List.of(new SchemeDTO());

        when(authentication.getName()).thenReturn("bank@example.com");
        when(bankService.getSchemesByBank("bank@example.com")).thenReturn(schemeList);

        ResponseEntity<?> response = bankController.getAllSchemesByBank(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(schemeList, response.getBody());
    }

    @Test
    void getAllApplyStatusByBank_ShouldReturnApplyList() {
        BankDTO bankDTO = new BankDTO();
        bankDTO.setId("bank-1");
        List<ApplyDTO> applyList = List.of(new ApplyDTO());

        when(authentication.getName()).thenReturn("bank@example.com");
        when(bankService.getBankByEmail("bank@example.com")).thenReturn(bankDTO);
        when(bankService.getApplyByBank(bankDTO.getId())).thenReturn(applyList);

        ResponseEntity<?> response = bankController.getAllApplyStatusByBank(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(applyList, response.getBody());
    }

    @Test
    void deleteScheme_ShouldReturnSuccessMessage() {
        Integer schemeId = 1;

        doNothing().when(bankService).deleteScheme(schemeId);

        ResponseEntity<String> response = bankController.deleteScheme(schemeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Scheme deleted successfully.", response.getBody());
    }

    @Test
    void updateApplyStatus_ShouldReturnSuccessMessage() {
        Integer applyId = 1;
        ApplyUpdateDTO updateDTO = new ApplyUpdateDTO();

        doNothing().when(bankService).updateApply(applyId, updateDTO);

        ResponseEntity<?> response = bankController.updateApplyStatus(applyId, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Apply status updated successfully !!", response.getBody());
    }

    @Test
    void getBankDashboardStats_ShouldReturnStatsMap() {
        String bankEmail = "bank@example.com";
        Bank bank = new Bank();
        bank.setId("bank-1");

        when(authentication.getName()).thenReturn(bankEmail);
        when(bankDao.findByEmail(bankEmail)).thenReturn(bank);
        when(schemeDao.countByBankId(bank.getId())).thenReturn(10L);
        when(applyDao.countByBankId(bank.getId())).thenReturn(50L);
        when(applyDao.countByBankIdAndStatus(bank.getId(), Status.APPROVED)).thenReturn(30L);
        when(applyDao.countByBankIdAndStatus(bank.getId(), Status.PENDING)).thenReturn(20L);

        ResponseEntity<Map<String, Long>> response = bankController.getBankDashboardStats(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Long> stats = response.getBody();
        assertNotNull(stats);
        assertEquals(10L, stats.get("totalSchemes"));
        assertEquals(50L, stats.get("totalApplications"));
        assertEquals(30L, stats.get("approved"));
        assertEquals(20L, stats.get("pending"));
    }

    @Test
    void getLoanFormDetailByFarmerId_ShouldReturnLoanForm() {
        String farmerId = "farmer-1";
        FarmerDTO farmerDTO = new FarmerDTO();
        farmerDTO.setEmail("farmer@example.com");
        LoanFormDTO loanFormDTO = new LoanFormDTO();

        when(farmerService.getFarmerById(farmerId)).thenReturn(farmerDTO);
        when(farmerService.getLoanFormByEmail(farmerDTO.getEmail())).thenReturn(loanFormDTO);

        ResponseEntity<?> response = bankController.getLoanFormDetailByFarmerId(farmerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loanFormDTO, response.getBody());
    }

    @Test
    void getLoanFormDetailByFarmerId_ShouldReturnNotFound_WhenFarmerNotFound() {
        String farmerId = "farmer-1";

        when(farmerService.getFarmerById(farmerId)).thenReturn(null);

        ResponseEntity<?> response = bankController.getLoanFormDetailByFarmerId(farmerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Farmer not found"));
    }

    @Test
    void getLoanFormDetailByFarmerId_ShouldReturnNotFound_WhenLoanFormNotFound() {
        String farmerId = "farmer-1";
        FarmerDTO farmerDTO = new FarmerDTO();
        farmerDTO.setEmail("farmer@example.com");

        when(farmerService.getFarmerById(farmerId)).thenReturn(farmerDTO);
        when(farmerService.getLoanFormByEmail(farmerDTO.getEmail())).thenReturn(null);

        ResponseEntity<?> response = bankController.getLoanFormDetailByFarmerId(farmerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Loan form not found"));
    }

    @Test
    void getGrievences_ShouldReturnGrievencesList() {
        String bankEmail = "bank@example.com";
        BankDTO bankDTO = new BankDTO();
        bankDTO.setId("bank-1");
        List<GrievencesDTO> grievencesList = List.of(new GrievencesDTO());

        when(authentication.getName()).thenReturn(bankEmail);
        when(bankService.getBankByEmail(bankEmail)).thenReturn(bankDTO);
        when(bankService.getGrievences(bankDTO.getId())).thenReturn(grievencesList);

        ResponseEntity<?> response = bankController.getGrievences(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grievencesList, response.getBody());
    }
}

