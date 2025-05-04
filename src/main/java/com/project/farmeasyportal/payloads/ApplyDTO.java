package com.project.farmeasyportal.payloads;

import java.time.LocalDate;

public class ApplyDTO {

    private int id;
    private String farmerId;
    private String schemeId;
    private String bankId;
    private LocalDate date;
    private String statusDate;
    private String amount;
    private String status;
    private String review;

    // Embedded related objects
    private FarmerDTO farmerDTO;
    private SchemeDTO schemeDTO;
    private BankDTO bankDTO;

}
