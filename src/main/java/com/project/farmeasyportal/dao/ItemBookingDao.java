package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.Item;
import com.project.farmeasyportal.entities.ItemBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemBookingDao extends JpaRepository<ItemBooking, Integer> {
    // In ItemRepository
    @Query("SELECT b FROM ItemBooking b JOIN Item i ON b.itemId = i.id WHERE i.merchantId = :merchantId")
    List<ItemBooking> findAllByMerchantId(@Param("merchantId") String merchantId);

}
