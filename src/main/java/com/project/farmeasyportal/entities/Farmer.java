package com.project.farmeasyportal.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FARMER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Farmer {

    @Id
    @Column(name = "USER_ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    /*@Column(name = "LAST_NAME")
    private String lastName;*/

    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "EMAIL", unique = true)
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ADDRESS")
    private String address;

    /*@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "farmer")
    private List<Apply> apply = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "farmer")
    private List<Grievences> grievences = new ArrayList<>();*/

    /*@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "farmer_scheme", joinColumns = @JoinColumn(name = "farmer", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "scheme", referencedColumnName = "id"))
    private Set<Scheme> scheme = new HashSet<>();*/

}
