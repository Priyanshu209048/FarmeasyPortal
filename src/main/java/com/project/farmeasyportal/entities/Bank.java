package com.project.farmeasyportal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String bankName;
    private String bankAddress;
    private String bankCity;
    private String bankState;
    private String bankZip;
    private String email;
    private String bankPhone;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bank")
    private List<Scheme> schemes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bank")
    private List<Apply> apply = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bank")
    private List<Grievences> grievences = new ArrayList<>();

    public Bank(int id, String bankName, String bankAddress, String bankCity, String bankState, String bankZip, String email, String bankPhone) {
        this.id = id;
        this.bankName = bankName;
        this.bankAddress = bankAddress;
        this.bankCity = bankCity;
        this.bankState = bankState;
        this.bankZip = bankZip;
        this.email = email;
        this.bankPhone = bankPhone;
    }
}
