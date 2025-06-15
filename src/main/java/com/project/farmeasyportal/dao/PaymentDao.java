package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDao extends JpaRepository<Payment, String> {
    Payment findByLendingRequestId(String bookingId);
}
