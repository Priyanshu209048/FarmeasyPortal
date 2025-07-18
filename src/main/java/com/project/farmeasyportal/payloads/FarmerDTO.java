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
public class FarmerDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @NotBlank(message = "First Name can not be empty !!")
    @Size(min = 2,max = 20,message = "First name must be between 2 - 20 characters")
    private String name;

    /*@NotBlank(message = "Last Name can not be empty !!")
    @Size(min = 2,max = 20,message = "Last name must be between 2 - 20 characters")
    private String lastName;*/

    @NotBlank(message = "Contact cannot be null")
    private String contact;

    @Column(unique = true)
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank(message = "E-Mail can not be empty !!")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 5, max = 15, message = "Password must contain minimum 5 characters and maximum of 15 characters ")
    private String password;

    @NotBlank(message = "Address is mandatory")
    private String address;

}
