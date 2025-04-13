package com.project.farmeasyportal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "apply")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @ManyToOne
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "status_date")
    private String statusDate;

    @Column(name = "amount", nullable = false)
    private String amount;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "review")
    private String review;
}
