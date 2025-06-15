package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.MyOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyOrderDao extends JpaRepository<MyOrder, Integer> {
    MyOrder findByOrderId(String razorpayOrderId);
}
