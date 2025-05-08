package com.project.farmeasyportal.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchemeDTO {

    private int id;
    private String schemeName;
    private String schemeCode;
    private String schemeDescription;
    private String benefits;
    private String eligibility;
    private String documents;
    private String roi;
    private String tenure;
    private String schemeType;

    private BankDTO bankDTO;

}
