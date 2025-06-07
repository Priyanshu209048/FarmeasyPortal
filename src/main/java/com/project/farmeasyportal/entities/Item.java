package com.project.farmeasyportal.entities;

import com.project.farmeasyportal.enums.ItemCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;
    private String description;
    private int totalQuantity;
    private BigDecimal pricePerDay;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ItemCategory category;

    private String merchantId;
}

