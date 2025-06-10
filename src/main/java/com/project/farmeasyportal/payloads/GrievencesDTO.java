package com.project.farmeasyportal.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    private FarmerDTO farmerDTO;

    private BankDTO bankDTO;

    @NotNull(message = "Date of grievance is required")
    private LocalDate grievencesDate;

    @NotBlank(message = "Grievance type cannot be blank")
    @Size(max = 50, message = "Grievance type can be at most 50 characters")
    private String grievencesType;

    @NotBlank(message = "Grievance description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String grievencesDescription;

    @NotBlank(message = "Grievance status is required")
    @Pattern(regexp = "PENDING|RESOLVED|REJECTED", message = "Status must be PENDING, RESOLVED, or REJECTED")
    private String grievencesStatus;

    @Size(max = 500, message = "Review can be at most 500 characters")
    private String grievencesReview;

}
