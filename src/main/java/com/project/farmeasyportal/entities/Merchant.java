package com.project.farmeasyportal.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {

    @Id
    private String id;

    private String name;
    private String phone;
    private String email;
    private String password;
    private String address;

}

