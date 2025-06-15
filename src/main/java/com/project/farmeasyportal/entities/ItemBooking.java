package com.project.farmeasyportal.entities;

import com.project.farmeasyportal.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int itemId;
    private String farmerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int quantityRequested;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status status = Status.PENDING;

    private BigDecimal totalCost;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status paymentStatus = Status.PENDING;

    @Column(name = "delivered_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status deliveredStatus = Status.PENDING;
}
