package com.project.farmeasyportal.payloads;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemBookingDTO {

    @Null(message = "ID is auto-generated and should not be provided")
    private String id;

    @NotNull(message = "Item details are required")
    private ItemDTO itemDTO;

    @NotNull(message = "Farmer details are required")
    private FarmerDTO farmerDTO;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @Min(value = 1, message = "At least one quantity must be requested")
    private int quantityRequested;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "PENDING|APPROVED|REJECTED|CANCELLED", message = "Status must be one of: PENDING, APPROVED, REJECTED, CANCELLED")
    private String status;

    @NotNull(message = "Total cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total cost must be greater than zero")
    private BigDecimal totalCost;

    @NotBlank(message = "Payment status is required")
    @Pattern(regexp = "PENDING|PAID|FAILED", message = "Payment status must be one of: PENDING, PAID, FAILED")
    private String paymentStatus;

}
