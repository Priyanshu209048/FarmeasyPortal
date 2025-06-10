package com.project.farmeasyportal.payloads;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchemeDTO {

    @Null(message = "ID is auto-generated and should not be provided")
    private int id;

    @NotBlank(message = "Scheme name is required")
    private String schemeName;

    @NotBlank(message = "Scheme code is required")
    private String schemeCode;

    @NotBlank(message = "Scheme description is required")
    private String schemeDescription;

    @NotBlank(message = "Benefits field is required")
    private String benefits;

    @NotBlank(message = "Eligibility information is required")
    private String eligibility;

    @NotNull(message = "Minimum salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Minimum salary must be positive")
    private BigDecimal min_salary;

    @NotNull(message = "Maximum salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Maximum salary must be positive")
    private BigDecimal max_salary;

    @Min(value = 0, message = "CIBIL score must be non-negative")
    private int cibil_score;

    @NotBlank(message = "Documents field is required")
    private String documents;

    @NotBlank(message = "Rate of interest is required")
    private String roi;

    @NotBlank(message = "Tenure is required")
    private String tenure;

    @NotBlank(message = "Scheme type is required")
    private String schemeType;

    private BankDTO bankDTO;

}
