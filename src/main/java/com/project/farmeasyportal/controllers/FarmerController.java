package com.project.farmeasyportal.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.farmeasyportal.constants.UsersConstants;
import com.project.farmeasyportal.dao.*;
import com.project.farmeasyportal.entities.Apply;
import com.project.farmeasyportal.entities.EvaluationRequest;
import com.project.farmeasyportal.entities.LoanForm;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import com.project.farmeasyportal.payloads.*;
import com.project.farmeasyportal.services.BankService;
import com.project.farmeasyportal.services.FarmerService;
import com.project.farmeasyportal.services.impl.DroolsService;
import com.project.farmeasyportal.services.impl.EvaluationRequestMapper;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/farmer")
@Log4j2
public class FarmerController {

    private final FarmerService farmerService;
    private final LoanFormDao loanFormDao;
    private final BankService bankService;
    private final ApplyDao applyDao;
    private final ModelMapper modelMapper;
    private final DroolsService droolsService;
    private final EvaluationRequestDao evaluationRequestDao;
    private final GrievencesDao grievencesDao;

    /*private static final Logger log = LoggerFactory.getLogger(FarmerController.class);*/

    @Autowired
    public FarmerController(FarmerService farmerService, LoanFormDao loanFormDao, BankService bankService, ApplyDao applyDao, ModelMapper modelMapper, DroolsService droolsService, EvaluationRequestDao evaluationRequestDao, GrievencesDao grievencesDao) {
        this.farmerService = farmerService;
        this.loanFormDao = loanFormDao;
        this.bankService = bankService;
        this.applyDao = applyDao;
        this.modelMapper = modelMapper;
        this.droolsService = droolsService;
        this.evaluationRequestDao = evaluationRequestDao;
        this.grievencesDao = grievencesDao;
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
    public ResponseEntity<?> loanFormProcess(@RequestPart("loanForm") @Valid String loanFormJson,
                                             @RequestPart("aadhaar") MultipartFile aadhaar,
                                             @RequestPart("pan") MultipartFile pan,
                                             @RequestPart("land") MultipartFile landDetails,
                                             Authentication authentication) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        LoanFormDTO loanFormDTO = mapper.readValue(loanFormJson, LoanFormDTO.class);

        if (!loanFormDTO.getEmail().equals(authentication.getName())) {
            log.error("Unauthorized access.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access.");
        }

        FarmerDTO farmerDTO = this.farmerService.getFarmerByEmail(authentication.getName());
        this.farmerService.submitForm(loanFormDTO, aadhaar, pan, landDetails, farmerDTO.getId());

        return new ResponseEntity<>("Loan form submitted successfully.", HttpStatus.CREATED);
    }

    @PutMapping(value = "/updateForm", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateLoanFormProcess(@RequestPart("loanFormDTO") @Valid @NonNull String loanFormJson,
                                                   @RequestPart("aadhaar") MultipartFile aadhaar,
                                                   @RequestPart("pan") MultipartFile pan,
                                                   @RequestPart("land") MultipartFile landDetails,
                                                   Authentication authentication) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        LoanFormDTO loanFormDTO = objectMapper.readValue(loanFormJson, LoanFormDTO.class);

        if (!loanFormDTO.getEmail().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access.");
        }

        FarmerDTO farmerDTO = this.farmerService.getFarmerByEmail(authentication.getName());
        LoanFormDTO updatedForm = this.farmerService.updateLoanForm(loanFormDTO, aadhaar, pan, landDetails, farmerDTO.getId());

        return new ResponseEntity<>(updatedForm, HttpStatus.OK);
    }

    @GetMapping("/getFormDetail")
    public ResponseEntity<?> getLoanFormDetail(Authentication authentication) {
        LoanFormDTO loanFormDTO = this.farmerService.getLoanFormByEmail(authentication.getName());
        return new ResponseEntity<>(loanFormDTO, HttpStatus.OK);
    }

    /*@GetMapping("/download")
    @ResponseBody
    public FileSystemResource download(@Param(value = "id") Integer id) {
        LoanForm loanForm = loanFormDao.findById(id).orElse(null);
        assert loanForm != null;
        return new FileSystemResource(new File(System.getProperty("user.dir") + "/src/main/resources/static/documents" + File.separator + loanForm.getLandDetailsPdf()));
    }*/

    @GetMapping("/download")
    @ResponseBody
    public FileSystemResource download(@RequestParam("id") Integer id, @RequestParam("documentType") String documentType) {
        LoanForm loanForm = loanFormDao.findById(id).orElseThrow(() -> new ResourceNotFoundException(UsersConstants.LOAN_FORM, UsersConstants.ID, id.toString()));

        String pdfFileName = getPdfFileName(documentType, loanForm);

        File file = new File(System.getProperty("user.dir") + "/src/main/resources/static/documents" + File.separator + pdfFileName);

        if (!file.exists()) {
            throw new ResourceNotFoundException("Document", "name", pdfFileName);
        }

        return new FileSystemResource(file);
    }

    private static String getPdfFileName(String documentType, LoanForm loanForm) {
        String pdfFileName = switch (documentType.toLowerCase()) {
            case "land" -> loanForm.getLandDetailsPdf();
            case "aadhaar" -> loanForm.getAadhaarPdfName();
            case "pan" -> loanForm.getPanPdfName();
            default -> throw new IllegalArgumentException("Invalid document type: " + documentType);
        };

        if (pdfFileName == null || pdfFileName.isEmpty()) {
            throw new IllegalArgumentException(documentType + " document not found for this loan form.");
        }
        return pdfFileName;
    }


    @GetMapping("/schemes")
    public ResponseEntity<?> getAllSchemes() {
        List<SchemeDTO> schemes = this.bankService.getSchemes();
        return new ResponseEntity<>(schemes, HttpStatus.OK);
    }

    @PostMapping("/apply/{schemeId}")
    public ResponseEntity<?> applyScheme(@PathVariable("schemeId") Integer schemeId, @RequestBody ApplyRequestDTO applyRequestDTO, Authentication authentication) {
        try {
            FarmerDTO farmerDTO = this.farmerService.getFarmerByEmail(authentication.getName());

            long appliedSchemeCount = this.applyDao.countByFarmerId(farmerDTO.getId());
            if (appliedSchemeCount >= 3)
                return new ResponseEntity<>("You cannot apply for more than 3 loan schemes.", HttpStatus.FORBIDDEN);

            LoanForm loanForm = this.loanFormDao.findByEmail(farmerDTO.getEmail());
            ApplyDTO applyDTO = this.farmerService.applyLoanScheme(schemeId, farmerDTO.getId(), applyRequestDTO.getAmount());

            EvaluationRequest request = EvaluationRequestMapper.fromEntities(loanForm, this.modelMapper.map(applyDTO, Apply.class));
            this.evaluationRequestDao.save(request);
            /*boolean approved = droolsService.executeRules(farmerDTO.getId(), request.getSchemeId(), request.getBankId());*/
            boolean pending = droolsService.executeRules(request, farmerDTO.getId());

            return new ResponseEntity<>(pending ? "Loan status is Pending" : "Loan is Rejected", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while processing the application.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/apply-status")
    public ResponseEntity<?> getApplyStatus(Authentication authentication) {
        String username = authentication.getName();
        FarmerDTO farmerDTO = this.farmerService.getFarmerByEmail(username);
        List<ApplyDTO> applyStatus = this.farmerService.getApplyStatus(farmerDTO.getId());
        return new ResponseEntity<>(applyStatus, HttpStatus.OK);
    }

    @PostMapping("/grievences")
    public ResponseEntity<?> processGrievences(@RequestBody @Valid GrievencesRequestDTO grievencesRequestDTO, Authentication authentication) {
        String username = authentication.getName();
        FarmerDTO farmerDTO = this.farmerService.getFarmerByEmail(username);

        this.farmerService.addGrievence(grievencesRequestDTO, farmerDTO);
        return new ResponseEntity<>("Your Grievence posted successfully !!", HttpStatus.OK);
    }

}
