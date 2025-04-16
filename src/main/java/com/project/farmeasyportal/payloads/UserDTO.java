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
public class UserDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank(message = "E-Mail can not be empty !!")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 4, max = 15, message = "Password must contain minimum 5 characters and maximum of 15 characters ")
    private String password;

    private String role;

}
