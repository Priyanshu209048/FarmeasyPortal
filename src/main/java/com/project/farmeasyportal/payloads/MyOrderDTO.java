package com.project.farmeasyportal.payloads;

import com.project.farmeasyportal.enums.Status;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyOrderDTO {

    private int myOrderId;

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotBlank(message = "Amount is required")
    private String amount;

    private String receipt;

    @NotNull(message = "Status is required")
    private Status status;

    @NotBlank(message = "User ID is required")
    private String userId;

    private String paymentId;

    private Integer bookingId;
}
