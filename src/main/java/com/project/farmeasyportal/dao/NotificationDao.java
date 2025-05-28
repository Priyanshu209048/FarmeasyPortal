package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationDao extends JpaRepository<Notification, Integer> {

    List<Notification> findByFarmerOrderByTimestampDesc(String farmerId);

}
