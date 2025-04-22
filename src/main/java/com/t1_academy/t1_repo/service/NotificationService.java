package com.t1_academy.t1_repo.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${notification.email}")
    private String notificationEmail; // хоть пока я и отправляю сам себе, но можно настроить на другого получателя

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendStatusChangeEmail(Long taskId, String status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(notificationEmail);
        message.setSubject("Task Status Updated");
        message.setText("Task ID " + taskId + " has changed status to: " + status);
        mailSender.send(message);
    }

}