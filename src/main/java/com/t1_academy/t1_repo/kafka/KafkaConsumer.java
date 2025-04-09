package com.t1_academy.t1_repo.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "task-status-updates", groupId = "task-group")
    public void listen(String message) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(message);
        Long taskId = node.get("taskId").asLong();
        String status = node.get("status").asText();

        notificationService.sendEmail(taskId, status);
    }
}

