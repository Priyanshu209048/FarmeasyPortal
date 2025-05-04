package com.project.farmeasyportal.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GrievencesDTO {

    private int id;
    private FarmerDTO farmerDTO;
    private BankDTO bankDTO;
    private LocalDate grievencesDate;
    private String grievencesType;
    private String grievencesDescription;
    private String grievencesStatus;
    private String grievencesReview;

}
