package com.t1_academy.t1_repo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendEmail(Long taskId, String newStatus) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("sibiryaqdev@mail.ru");
        message.setSubject("Статус задачи изменён");
        message.setText("Задача с ID " + taskId + " теперь имеет статус: " + newStatus);

        mailSender.send(message);
    }
}
