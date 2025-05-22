package com.project.farmeasyportal.payloads;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplyDTO {

    private int id;
    private String farmerId;
    private int schemeId;
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
