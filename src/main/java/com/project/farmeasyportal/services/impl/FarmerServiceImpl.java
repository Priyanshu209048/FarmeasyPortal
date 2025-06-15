package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.constants.PdfConstants;
import com.project.farmeasyportal.constants.UsersConstants;
import com.project.farmeasyportal.dao.*;
import com.project.farmeasyportal.entities.*;
import com.project.farmeasyportal.enums.Status;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.*;
import com.project.farmeasyportal.services.FarmerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FarmerServiceImpl implements FarmerService {

    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/documents";

    private final FarmerDao farmerDao;
    private final UserDao userDao;
    private final LoanFormDao loanFormDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final GrievencesDao grievencesDao;
    private final ApplyDao applyDao;
    private final BankDao bankDao;
    private final SchemeDao schemeDao;
    private final ItemDao itemDao;
    private final ItemBookingDao itemBookingDao;
    private final MerchantDao merchantDao;

    @Override
    public FarmerDTO saveFarmer(FarmerDTO farmerDTO) {
        if (farmerDTO.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(farmerDTO.getPassword());

        farmerDTO.setPassword(encodedPassword);
        farmerDTO.setId(String.valueOf(UUID.randomUUID()));
        Farmer farmer = modelMapper.map(farmerDTO, Farmer.class);

        User user = new User();
        user.setEmail(farmer.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(UsersConstants.ROLE_FARMER);
        userDao.save(user);
        Farmer save = farmerDao.save(farmer);
        return this.modelMapper.map(save, FarmerDTO.class);
    }

    @Override
    public FarmerDTO updateFarmer(String id, FarmerDTO farmerDTO) {
        Farmer farmer = this.farmerDao.findById(id).orElseThrow(() -> new ResourceNotFoundException(UsersConstants.FARMER, UsersConstants.ID, id));

        farmer.setFirstName(farmerDTO.getFirstName());
        farmer.setLastName(farmerDTO.getLastName());
        farmer.setContact(farmerDTO.getContact());
        Farmer update = farmerDao.save(farmer);

        return this.modelMapper.map(update, FarmerDTO.class);
    }

    @Override
    public FarmerDTO getFarmerByEmail(String email) {
        Farmer farmer = this.farmerDao.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(UsersConstants.FARMER, UsersConstants.EMAIL, email));
        return this.modelMapper.map(farmer, FarmerDTO.class);
    }

    @Override
    public FarmerDTO getFarmerById(String id) {
        Farmer farmer = this.farmerDao.findById(id).orElseThrow(() -> new ResourceNotFoundException(UsersConstants.FARMER, UsersConstants.ID, id));
        return this.modelMapper.map(farmer, FarmerDTO.class);
    }

    @Override
    public List<FarmerDTO> getAllFarmers() {
        List<Farmer> farmers = this.farmerDao.findAll();
        return farmers.stream().map(farmer -> this.modelMapper.map(farmer, FarmerDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteFarmer(String id) {
        Farmer farmer = this.farmerDao.findById(id).orElseThrow(() -> new ResourceNotFoundException(UsersConstants.FARMER, UsersConstants.ID, id));
        this.userDao.delete(this.userDao.findByEmail(farmer.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(UsersConstants.USER, UsersConstants.EMAIL, farmer.getEmail())));
        this.farmerDao.delete(farmer);
    }

    @Override
    public Boolean isFarmerExistByEmail(String email) {
        return this.farmerDao.existsByEmail(email);
    }

    @Override
    public Boolean isFarmerExistById(String id) {
        return this.farmerDao.existsById(id);
    }

    public String savePdf(MultipartFile landDetails, String landDetailsOriginalFileName, String pdfName, String userId) throws IOException {
        String storedPdfName;
        if (!landDetails.isEmpty()) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            storedPdfName = pdfName + UsersConstants.DASH + userId + UsersConstants.DASH + timestamp + ".pdf";

            Path directoryPath = Paths.get(uploadDir);
            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = directoryPath.resolve(storedPdfName);
            Files.write(filePath, landDetails.getBytes());
        } else {
            storedPdfName = landDetailsOriginalFileName;
        }

        return storedPdfName;
    }

    @Override
    public void submitForm(LoanFormDTO loanFormDTO, MultipartFile aadhaar, MultipartFile pan, MultipartFile landDetails, String userId) throws IOException {
        LoanForm loanForm = this.modelMapper.map(loanFormDTO, LoanForm.class);

        String aadhaarOriginalFilename = aadhaar.getOriginalFilename();
        String panOriginalFilename = pan.getOriginalFilename();
        String landDetailsOriginalFilename = landDetails.getOriginalFilename();

        /*String storedPdfName;

        if (!file.isEmpty()) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            storedPdfName = userId + "_" + timestamp + ".pdf";

            Path directoryPath = Paths.get(uploadDir);
            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = directoryPath.resolve(storedPdfName);
            Files.write(filePath, file.getBytes());
        } else {
            storedPdfName = originalFileName;
        }*/

        String saveAadhaarPdf = savePdf(aadhaar, aadhaarOriginalFilename, PdfConstants.AADHAAR, userId);
        String savePanPdf = savePdf(pan, panOriginalFilename, PdfConstants.PAN, userId);
        String landDetailsPdf = savePdf(landDetails, landDetailsOriginalFilename, PdfConstants.LAND_DETAILS, userId);

        loanForm.setAadhaarPdfName(saveAadhaarPdf);
        loanForm.setPanPdfName(savePanPdf);
        loanForm.setLandDetailsPdf(landDetailsPdf);

        loanFormDao.save(loanForm);
    }

    @Override
    public Boolean isUserSubmittedForm(String email) {
        return loanFormDao.existsByEmail(email);
    }

    @Override
    public LoanFormDTO getLoanFormByEmail(String email) {
        LoanForm loanForm = loanFormDao.findByEmail(email);
        return this.modelMapper.map(loanForm, LoanFormDTO.class);
    }

    @Override
    public LoanFormDTO updateLoanForm(LoanFormDTO loanFormDTO, MultipartFile aadhaar, MultipartFile pan, MultipartFile landDetails, String userId) throws IOException {
        LoanForm existingLoanForm = this.loanFormDao.findById(loanFormDTO.getId()).orElseThrow(() -> new ResourceNotFoundException(UsersConstants.LOAN_FORM, UsersConstants.ID, String.valueOf(loanFormDTO.getId())));
        String name = existingLoanForm.getName();
        String email = existingLoanForm.getEmail();
        String contact = existingLoanForm.getContact();
        String gender = existingLoanForm.getGender();

        LoanForm loanForm = this.modelMapper.map(loanFormDTO, LoanForm.class);

        loanForm.setId(existingLoanForm.getId());
        loanForm.setName(name);
        loanForm.setEmail(email);
        loanForm.setContact(contact);
        loanForm.setGender(gender);

        String aadhaarOriginalFilename = aadhaar.isEmpty() ? loanFormDTO.getAadhaarPdfName() : aadhaar.getOriginalFilename();
        String panOriginalFilename = pan.isEmpty() ? loanFormDTO.getPanPdfName() : pan.getOriginalFilename();
        String landDetailsOriginalFilename = landDetails.isEmpty() ? loanFormDTO.getLandDetailsPdf() : landDetails.getOriginalFilename();

        /*String storedPdfName;

        if (!file.isEmpty()) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            storedPdfName = userId + "_" + timestamp + ".pdf";

            Path directoryPath = Paths.get(uploadDir);
            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = directoryPath.resolve(storedPdfName);
            Files.write(filePath, file.getBytes());
        } else {
            storedPdfName = originalFileName;
        }

        loanForm.setPdfName(storedPdfName);*/

        String saveAadhaarPdf = savePdf(aadhaar, aadhaarOriginalFilename, PdfConstants.AADHAAR, userId);
        String savePanPdf = savePdf(pan, panOriginalFilename, PdfConstants.PAN, userId);
        String landDetailsPdf = savePdf(landDetails, landDetailsOriginalFilename, PdfConstants.LAND_DETAILS, userId);

        loanForm.setAadhaarPdfName(saveAadhaarPdf);
        loanForm.setPanPdfName(savePanPdf);
        loanForm.setLandDetailsPdf(landDetailsPdf);

        loanFormDao.save(loanForm);

        return modelMapper.map(loanForm, LoanFormDTO.class);
    }

    @Override
    public ApplyDTO applyLoanScheme(Integer schemeId, String farmerId, String amount) {
        Scheme scheme = this.schemeDao.findById(schemeId).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.SCHEME, UsersConstants.ID, String.valueOf(schemeId)));
        Farmer farmer = this.farmerDao.findById(farmerId).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.FARMER, UsersConstants.ID, farmerId));
        Bank bank = this.bankDao.findById(scheme.getBankId()).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.BANK, UsersConstants.ID, scheme.getBankId()));

        Apply apply = new Apply();
        apply.setFarmerId(farmerId);
        apply.setSchemeId(schemeId);
        apply.setBankId(scheme.getBankId());
        apply.setDate(LocalDate.now());
        apply.setAmount(amount);
        apply.setReview(UsersConstants.DASH);
        apply.setStatusDate(UsersConstants.DASH);
        apply.setStatus(Status.PENDING);
        Apply save = this.applyDao.save(apply);

        ApplyDTO applyDTO = this.modelMapper.map(save, ApplyDTO.class);
        applyDTO.setSchemeDTO(this.modelMapper.map(scheme, SchemeDTO.class));
        applyDTO.setFarmerDTO(this.modelMapper.map(farmer, FarmerDTO.class));
        applyDTO.setBankDTO(this.modelMapper.map(bank, BankDTO.class));
        return applyDTO;
    }

    @Override
    public List<ApplyDTO> getApplyStatus(String farmerId) {
        List<Apply> applyList = this.applyDao.findAllByFarmerId(farmerId);
        return applyList.stream().map(apply -> {
            ApplyDTO applyDTO = this.modelMapper.map(apply, ApplyDTO.class);
            Farmer farmer = this.farmerDao.findById(farmerId).orElseThrow(() ->
                    new ResourceNotFoundException(UsersConstants.FARMER, UsersConstants.ID, farmerId));

            Bank bank = this.bankDao.findById(apply.getBankId()).orElseThrow(() ->
                    new ResourceNotFoundException(UsersConstants.BANK, UsersConstants.ID, apply.getBankId()));

            Scheme scheme = this.schemeDao.findById(Integer.valueOf(apply.getSchemeId())).orElseThrow(() ->
                    new ResourceNotFoundException(UsersConstants.SCHEME, UsersConstants.ID, String.valueOf(apply.getSchemeId())));

            applyDTO.setFarmerDTO(this.modelMapper.map(farmer, FarmerDTO.class));
            applyDTO.setBankDTO(this.modelMapper.map(bank, BankDTO.class));
            applyDTO.setSchemeDTO(this.modelMapper.map(scheme, SchemeDTO.class));
            return applyDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public ItemBookingDTO itemBooking(ItemBookingDTO itemBookingDTO, String farmerId, int itemId) {
        ItemBooking itemBooking = this.modelMapper.map(itemBookingDTO, ItemBooking.class);
        Item item = this.itemDao.findById(itemId).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.ITEM, UsersConstants.ID, String.valueOf(itemId)));
        Merchant merchant = this.merchantDao.findById(item.getMerchantId()).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.MERCHANT, UsersConstants.ID, item.getMerchantId()));
        Farmer farmer = this.farmerDao.findById(farmerId).orElseThrow(() ->
                new ResourceNotFoundException(UsersConstants.FARMER, UsersConstants.ID, farmerId));

        LocalDate startDate = itemBooking.getStartDate();
        LocalDate endDate = itemBooking.getEndDate();

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date must not be null");
        }

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetween < 1) {
            throw new IllegalArgumentException("End Date must be after the start date");
        }

        BigDecimal totalCost = item.getPricePerDay()
                .multiply(BigDecimal.valueOf(itemBooking.getQuantityRequested()))
                .multiply(BigDecimal.valueOf(daysBetween));

        itemBooking.setItemId(itemId);
        itemBooking.setFarmerId(farmerId);
        itemBooking.setStatus(Status.PENDING);
        itemBooking.setPaymentStatus(Status.PENDING);
        itemBooking.setDeliveredStatus(Status.PENDING);
        itemBooking.setTotalCost(totalCost);

        ItemBooking save = this.itemBookingDao.save(itemBooking);
        ItemBookingDTO map = this.modelMapper.map(save, ItemBookingDTO.class);

        ItemDTO itemDTO = this.modelMapper.map(item, ItemDTO.class);
        itemDTO.setMerchantDTO(this.modelMapper.map(merchant, MerchantDTO.class));

        map.setItemDTO(itemDTO);
        map.setFarmerDTO(this.modelMapper.map(farmer, FarmerDTO.class));

        return map;
    }


    @Override
    public void addGrievence(GrievencesRequestDTO grievencesRequestDTO, FarmerDTO farmerDTO) {
        Farmer farmer = this.modelMapper.map(farmerDTO, Farmer.class);

        Grievences grievences = new Grievences();
        grievences.setFarmerId(farmer.getId());
        grievences.setBankId(grievencesRequestDTO.getBankId());
        grievences.setGrievencesType(grievencesRequestDTO.getGrievencesType());
        grievences.setGrievencesDescription(grievencesRequestDTO.getGrievencesDescription());
        grievences.setGrievencesDate(LocalDate.now());
        grievences.setGrievencesReview(UsersConstants.DASH);
        grievences.setGrievencesStatus(UsersConstants.PENDING);

        this.grievencesDao.save(grievences);
    }
}
