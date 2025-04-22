package com.t1_academy.t1_repo.kafka;

import com.t1_academy.t1_repo.model.dto.TaskStatusUpdateDTO;
import com.t1_academy.t1_repo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTaskConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${task.kafka.topic.status-updated}",
            groupId = "${task.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(List<TaskStatusUpdateDTO> messages, Acknowledgment ack) {
        log.info("KafkaConsumer: received {} messages from topic", messages.size());

        try {
            for (TaskStatusUpdateDTO dto : messages) {
                log.info("Processing message: {}", dto);
                notificationService.sendStatusChangeEmail(dto.getTaskId(), dto.getNewStatus());
            }
        } catch (Exception e) {
            log.error("Error while processing Kafka messages", e);
        } finally {
            ack.acknowledge(); // вручную подтверждаем offset
        }
    }
}
