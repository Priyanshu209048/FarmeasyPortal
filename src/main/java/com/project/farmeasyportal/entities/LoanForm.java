package com.project.farmeasyportal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loan_form")
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
    private int age;
    private int cibil;
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
