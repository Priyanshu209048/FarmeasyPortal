package com.project.farmeasyportal.payloads;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    @Null(message = "ID is auto-generated and should not be provided")
    private String id;

    @NotBlank(message = "Lending request ID is required")
    private String lendingRequestId;

    @Positive(message = "Amount must be greater than 0")
    private double amount;

    @NotBlank(message = "Payment mode is required")
    @Pattern(
            regexp = "^(UPI|CARD|NET_BANKING|CASH|WALLET)$",
            message = "Payment mode must be one of: UPI, CARD, NET_BANKING, CASH, WALLET"
    )
    private String paymentMode;

    @NotBlank(message = "Status is required")
    @Pattern(
            regexp = "^(PENDING|COMPLETED|FAILED)$",
            message = "Status must be one of: PENDING, COMPLETED, FAILED"
    )
    private String status;

    @PastOrPresent(message = "Payment time cannot be in the future")
    private LocalDateTime paymentTime;

}

