package com.project.farmeasyportal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "grievences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Grievences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @Column(name = "grievences_date", nullable = false)
    private LocalDate grievencesDate;

    @Column(name = "grievences_type", nullable = false)
    private String grievencesType;

    @Column(name = "grievences_description", nullable = false)
    private String grievencesDescription;

    @Column(name = "grievences_status")
    private String grievencesStatus;

    @Column(name = "grievences_review")
    private String grievencesReview;

}
