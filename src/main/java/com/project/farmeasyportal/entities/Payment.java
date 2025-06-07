package com.project.farmeasyportal.entities;

import com.project.farmeasyportal.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String lendingRequestId;
    private double amount;
    private String paymentMode;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status status = Status.PENDING;

    private LocalDateTime paymentTime;
}

