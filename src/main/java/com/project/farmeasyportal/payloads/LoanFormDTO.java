package com.project.farmeasyportal.payloads;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanFormDTO {

    @Null(message = "ID is auto-generated and should not be provided")
    private int id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit contact number")
    private String contact;

    @NotBlank(message = "Gender is required")
    private String gender;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be less than or equal to 100")
    private int age;

    @Min(value = 300, message = "CIBIL score must be at least 300")
    @Max(value = 900, message = "CIBIL score must not exceed 900")
    private int cibil;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pin code is required")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Enter a valid 6-digit PIN code")
    private String pinCode;

    @NotNull(message = "Aadhaar number is required")
    @Digits(integer = 12, fraction = 0, message = "Aadhaar must be a 12-digit number")
    private long aadhaarNumber;

    @NotBlank(message = "Aadhaar PDF name is required")
    private String aadhaarPdfName;

    @NotNull(message = "PAN number is required")
    @Digits(integer = 10, fraction = 0, message = "PAN number must be a 10-digit number")
    private long panNumber;

    @NotBlank(message = "PAN PDF name is required")
    private String panPdfName;

    @Min(value = 0, message = "Salary must not be negative")
    private long salary;

    @NotBlank(message = "Collateral type is required")
    private String collateralType;

    @NotBlank(message = "Guarantor name is required")
    private String guarantorName;

    @NotBlank(message = "Guarantor contact is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit guarantor contact")
    private String guarantorContact;

    @NotBlank(message = "Guarantor relation is required")
    private String guarantorRelation;

    @NotBlank(message = "Land amount is required")
    private String landAmount;

    @NotBlank(message = "Khasra number is required")
    private String khsraNumber;

    @NotBlank(message = "Land ownership is required")
    private String landOwnership;

    @NotBlank(message = "Soil type is required")
    private String soilType;

    @NotBlank(message = "Crop type is required")
    private String cropType;

    @NotBlank(message = "Land details PDF name is required")
    private String landDetailsPdf;

}
