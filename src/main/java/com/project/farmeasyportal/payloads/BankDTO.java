package com.project.farmeasyportal.payloads;

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
public class BankDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @NotBlank(message = "Bank Name can not be empty !!")
    @Size(min = 3,max = 20,message = "Bank name must be between 2 - 20 characters")
    private String bankName;

    @NotBlank(message = "Bank address can not be empty !!")
    private String bankAddress;

    @NotBlank(message = "Bank city can not be empty !!")
    private String bankCity;

    @NotBlank(message = "Bank state can not be empty !!")
    private String bankState;

    @NotBlank(message = "Bank zip can not be empty !!")
    private String bankZip;

    @Column(unique = true)
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank(message = "E-Mail can not be empty !!")
    private String email;

    @NotBlank(message = "Bank phone number cannot be null")
    private String bankPhone;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 5, max = 15, message = "Password must contain minimum 5 characters and maximum of 15 characters ")
    private String password;

}
