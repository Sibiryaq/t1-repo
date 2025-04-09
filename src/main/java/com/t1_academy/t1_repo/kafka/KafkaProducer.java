package com.t1_academy.t1_repo.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t1_academy.t1_repo.model.entity.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendStatusUpdate(Long taskId, TaskStatus status) {
        try {
            Map<String, Object> message = Map.of(
                    "taskId", taskId,
                    "status", status.toString()
            );
            String payload = objectMapper.writeValueAsString(message);
            kafkaTemplate.send("task-status-updates", payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации Kafka-сообщения", e);
        }
    }
}
