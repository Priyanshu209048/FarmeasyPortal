package com.project.farmeasyportal.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanFormDTO {

    private int id;
    private String name;
    private String email;
    private String contact;
    private String gender;
    private String address;
    private String district;
    private String state;
    private String pinCode;
    private long aadhaarNumber;
    private String aadhaarPdfName;
    private long panNumber;
    private String panPdfName;
    private long salary;

    private String collateralType;
    private String guarantorName;
    private String guarantorContact;
    private String guarantorRelation;

    private String landAmount;
    private String khsraNumber;
    private String landOwnership;
    private String soilType;
    private String cropType;
    private String landDetailsPdf;

}
