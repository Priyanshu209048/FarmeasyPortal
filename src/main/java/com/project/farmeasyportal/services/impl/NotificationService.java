package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.dao.NotificationDao;
import com.project.farmeasyportal.entities.Farmer;
import com.project.farmeasyportal.entities.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationDao notificationDao;

    public void createNotification(Farmer farmer, String shortMsg, String fullMsg) {
        Notification notification = new Notification();
        notification.setFarmer(farmer.getId());
        notification.setShortMessage(shortMsg);
        notification.setFullMessage(fullMsg);
        notification.setTimestamp(LocalDateTime.now());
        notificationDao.save(notification);
    }

    public List<Notification> getNotificationsForFarmer(String farmerId) {
        return notificationDao.findByFarmerOrderByTimestampDesc(farmerId);
    }

    public Notification getNotificationById(int id) {
        return notificationDao.findById(id).orElse(null);
    }

    public void markAsRead(int id) {
        Notification n = notificationDao.findById(id).orElse(null);
        if (n != null) {
            n.setRead(true);
            notificationDao.save(n);
        }
    }
}

