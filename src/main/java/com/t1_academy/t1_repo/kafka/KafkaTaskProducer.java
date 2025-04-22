package com.t1_academy.t1_repo.kafka;

import com.t1_academy.t1_repo.model.dto.TaskStatusUpdateDTO;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTaskProducer {

    private final KafkaTemplate<String, TaskStatusUpdateDTO> kafkaTemplate;

    public void sendTo(String topic, TaskStatusUpdateDTO dto) {
        try {
            kafkaTemplate.send(topic, dto); //без get() т.к. это блокирующий вызов
            kafkaTemplate.flush();
            log.info("KafkaProducer: message sent to topic [{}]: {}", topic, dto);
        } catch (Exception ex) {
            log.error("KafkaProducer: error while sending message - {}", ex.getMessage(), ex);
        }
    }
}
