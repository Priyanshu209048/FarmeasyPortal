package com.project.farmeasyportal.controllers;

import com.project.farmeasyportal.dao.ItemBookingDao;
import com.project.farmeasyportal.dao.MyOrderDao;
import com.project.farmeasyportal.dao.PaymentDao;
import com.project.farmeasyportal.entities.ItemBooking;
import com.project.farmeasyportal.entities.MyOrder;
import com.project.farmeasyportal.entities.Payment;
import com.project.farmeasyportal.enums.Status;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    /*@Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        com.stripe.Stripe.apiKey = stripeSecretKey;
    }*/

    private final ItemBookingDao itemBookingDao;
    private final PaymentDao paymentDao;
    private final MyOrderDao myOrderDao;

    @PostMapping("/create/{bookingId}")
    public ResponseEntity<?> createPaymentIntent(@PathVariable Integer bookingId) {
        try {
            Optional<ItemBooking> bookingOpt = itemBookingDao.findById(bookingId);
            if (bookingOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Booking not found"));
            }
            ItemBooking booking = bookingOpt.get();

            BigDecimal totalCost = booking.getTotalCost();
            if (totalCost == null || totalCost.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid booking total cost"));
            }

            long amount = totalCost.multiply(new BigDecimal(100)).longValue();

            String userId = booking.getFarmerId();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency("inr")
                    .putMetadata("userId", userId)
                    .putMetadata("bookingId", String.valueOf(bookingId))
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            MyOrder myOrder = MyOrder.builder()
                    .orderId(paymentIntent.getId())
                    .amount(String.valueOf(paymentIntent.getAmount()))
                    .receipt(paymentIntent.getClientSecret())
                    .status(Status.PENDING)
                    .userId(userId)
                    .bookingId(bookingId)
                    .build();
            myOrderDao.save(myOrder);

            return ResponseEntity.ok(Map.of(
                    "clientSecret", paymentIntent.getClientSecret(),
                    "orderId", paymentIntent.getId()
            ));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to create payment intent"));
        }
    }

    /**
     * Update payment status after payment is confirmed.
     * Expects a JSON body with paymentIntentId, status, paymentMode.
     */
    @PostMapping("/update")
    @Transactional
    public ResponseEntity<?> updatePayment(@RequestBody Map<String, Object> data) {
        try {
            String paymentIntentId = data.get("paymentIntentId").toString();
            String statusStr = data.get("status").toString();
            String paymentMode = data.get("paymentMode").toString();

            MyOrder myOrder = myOrderDao.findByOrderId(paymentIntentId);
            if (myOrder == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Order not found for paymentIntentId"));
            }

            Status paymentStatus;
            if ("succeeded".equalsIgnoreCase(statusStr)) {
                paymentStatus = Status.COMPLETED;
            } else if ("failed".equalsIgnoreCase(statusStr)) {
                paymentStatus = Status.FAILED;
            } else {
                paymentStatus = Status.PENDING;
            }

            myOrder.setPaymentId(paymentIntentId);
            myOrder.setStatus(paymentStatus);
            myOrderDao.save(myOrder);

            Payment payment = Payment.builder()
                    .lendingRequestId(String.valueOf(myOrder.getBookingId()))
                    .amount(Double.parseDouble(myOrder.getAmount()) / 100.0)
                    .paymentMode(paymentMode)
                    .status(paymentStatus)
                    .paymentTime(LocalDateTime.now())
                    .build();

            paymentDao.save(payment);

            Optional<ItemBooking> bookingOpt = itemBookingDao.findById(myOrder.getBookingId());
            if (bookingOpt.isPresent()) {
                ItemBooking booking = bookingOpt.get();
                booking.setPaymentStatus(paymentStatus);
                itemBookingDao.save(booking);
            }

            return ResponseEntity.ok(Map.of("msg", "Payment updated successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update payment: " + e.getMessage()));
        }
    }
}
