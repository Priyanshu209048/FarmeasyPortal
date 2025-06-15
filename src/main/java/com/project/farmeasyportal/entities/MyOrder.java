package com.project.farmeasyportal.entities;

import com.project.farmeasyportal.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int myOrderId;

	@Column(nullable = false, unique = true)
	private String orderId;

	@Column(nullable = false)
	private String amount;

	private String receipt;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private Status status;

	@Column(nullable = false)
	private String userId;

	private String paymentId;

	private Integer bookingId;
}
