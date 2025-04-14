package com.project.farmeasyportal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LoanTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    private String contact;
    private String gender;
    private String address;
    private String district;
    private String state;
    private String pinCode;
    private String idType;
    private String idNumber;

    private String collateralType;
    private String guarantorName;
    private String guarantorContact;
    private String guarantorRelation;

    private String landAmount;
    private String khsraNumber;
    private String landOwnership;
    private String soilType;
    private String cropType;
    private String pdfName;

}
