package com.project.farmeasyportal.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.project.farmeasyportal.constants.UsersConstants;
import com.project.farmeasyportal.dao.*;
import com.project.farmeasyportal.entities.*;
import com.project.farmeasyportal.enums.Status;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class BankServiceImplTest {

    @Mock
    private SchemeDao schemeDao;
    @Mock
    private BankDao bankDao;
    @Mock
    private ApplyDao applyDao;
    @Mock
    private UserDao userDao;
    @Mock
    private FarmerDao farmerDao;
    @Mock
    private NotificationService notificationService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private GrievencesDao grievencesDao;
    @InjectMocks
    private BankServiceImpl bankService;

    private Bank bankEntity;
    private BankDTO bankDTO;
    private User userEntity;

    @BeforeEach
    void setup() {
        bankEntity = new Bank();
        bankEntity.setId("bank-123");
        bankEntity.setEmail("bank@example.com");
        bankEntity.setBankName("Test Bank");
        bankEntity.setPassword("encodedPassword");

        bankDTO = new BankDTO();
        bankDTO.setId("bank-123");
        bankDTO.setEmail("bank@example.com");
        bankDTO.setBankName("Test Bank");
        bankDTO.setPassword("rawPassword");

        userEntity = new User();
        userEntity.setEmail(bankEntity.getEmail());
        userEntity.setPassword(bankEntity.getPassword());
        userEntity.setRole(UsersConstants.ROLE_BANK);
    }

    @Test
    void addBank_ShouldThrowIllegalArgumentException_WhenPasswordIsNull() {
        bankDTO.setPassword(null);

        assertThrows(IllegalArgumentException.class, () -> bankService.addBank(bankDTO));
        verifyNoInteractions(bCryptPasswordEncoder, userDao, bankDao);
    }

    @Test
    void getBankById_ShouldReturnBankDTO_WhenBankExists() {
        when(bankDao.findById("bank-123")).thenReturn(Optional.of(bankEntity));
        when(modelMapper.map(bankEntity, BankDTO.class)).thenReturn(bankDTO);

        BankDTO result = bankService.getBankById("bank-123");

        assertNotNull(result);
        assertEquals(bankDTO.getId(), result.getId());
    }

    @Test
    void getBankById_ShouldThrowResourceNotFound_WhenBankDoesNotExist() {
        when(bankDao.findById("bank-123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bankService.getBankById("bank-123"));
    }

    @Test
    void getBanks_ShouldReturnListOfBankDTO() {
        List<Bank> banks = List.of(bankEntity);
        List<BankDTO> bankDTOs = List.of(bankDTO);

        when(bankDao.findAll()).thenReturn(banks);
        when(modelMapper.map(bankEntity, BankDTO.class)).thenReturn(bankDTO);

        List<BankDTO> result = bankService.getBanks();

        assertEquals(1, result.size());
        assertEquals(bankDTO.getId(), result.get(0).getId());
    }

    @Test
    void isBankExistById_ShouldReturnTrue_WhenExists() {
        when(bankDao.existsById("bank-123")).thenReturn(true);

        assertTrue(bankService.isBankExistById("bank-123"));
    }

    @Test
    void isBankExistByEmail_ShouldReturnFalse_WhenNotExists() {
        when(bankDao.existsByEmail("notfound@example.com")).thenReturn(false);

        assertFalse(bankService.isBankExistByEmail("notfound@example.com"));
    }

    @Test
    void updateApply_ShouldUpdateStatusAndSendNotification_WhenApplyExists() {
        Integer applyId = 1;
        Apply applyEntity = new Apply();
        applyEntity.setId(applyId);
        applyEntity.setFarmerId("farmer-1");
        applyEntity.setBankId("bank-123");
        applyEntity.setSchemeId(100);
        applyEntity.setStatus(Status.PENDING);

        ApplyUpdateDTO updateDTO = new ApplyUpdateDTO();
        updateDTO.setStatus(Status.APPROVED.getCode());
        updateDTO.setReview("Approved by bank");

        Farmer farmer = new Farmer();
        farmer.setId("farmer-1");
        farmer.setName("John Doe");
        farmer.setEmail("farmer@example.com");

        Scheme scheme = new Scheme();
        scheme.setId(100);
        scheme.setSchemeName("Scheme A");

        Bank bank = new Bank();
        bank.setId("bank-123");
        bank.setBankName("Test Bank");

        List<Apply> pendingApplies = new ArrayList<>();

        when(applyDao.findById(applyId)).thenReturn(Optional.of(applyEntity));
        when(farmerDao.findById("farmer-1")).thenReturn(Optional.of(farmer));
        when(schemeDao.findById(100)).thenReturn(Optional.of(scheme));
        when(bankDao.findById("bank-123")).thenReturn(Optional.of(bank));
        when(applyDao.findAllByFarmerIdAndStatus("farmer-1", Status.PENDING)).thenReturn(pendingApplies);

        // Act
        bankService.updateApply(applyId, updateDTO);

        // Assert
        assertEquals(Status.APPROVED, applyEntity.getStatus());
        assertEquals("Approved by bank", applyEntity.getReview());
        verify(notificationService).createNotification(eq(farmer), anyString(), anyString());
        verify(applyDao).save(applyEntity);
    }

    @Test
    void updateApply_ShouldThrowException_WhenApplyNotFound() {
        when(applyDao.findById(1)).thenReturn(Optional.empty());

        ApplyUpdateDTO dto = new ApplyUpdateDTO();
        dto.setStatus(Status.APPROVED.getCode());
        dto.setReview("Test");

        assertThrows(ResourceNotFoundException.class, () -> bankService.updateApply(1, dto));
    }
}
