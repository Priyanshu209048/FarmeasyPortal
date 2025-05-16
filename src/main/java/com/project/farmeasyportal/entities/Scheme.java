package com.project.farmeasyportal.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scheme")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Scheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String schemeName;
    private String schemeCode;
    private String schemeDescription;
    private String benefits;
    private String eligibility;
    private BigDecimal min_salary;
    private BigDecimal max_salary;
    private int cibil_score;
    private String documents;
    private String roi;
    private String tenure;
    private String schemeType;

    @Column(name = "bank_id", nullable = false)
    @NotNull
    private String bankId;

    /*@ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "scheme")
    private List<Apply> apply = new ArrayList<>();*/

}
