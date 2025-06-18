package com.project.farmeasyportal.controllers;

import com.project.farmeasyportal.entities.Notification;
import com.project.farmeasyportal.payloads.FarmerDTO;
import com.project.farmeasyportal.services.FarmerService;
import com.project.farmeasyportal.services.impl.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final FarmerService farmerService;

    @GetMapping("/")
    public List<Notification> getAllNotifications(Authentication authentication) {
        String username = authentication.getName();
        FarmerDTO farmerDTO = this.farmerService.getFarmerByEmail(username);
        return notificationService.getNotificationsForFarmer(farmerDTO.getId());
    }

    @GetMapping("/detail/{id}")
    public Notification getNotification(@PathVariable int id) {
        notificationService.markAsRead(id);
        return notificationService.getNotificationById(id);
    }
}
