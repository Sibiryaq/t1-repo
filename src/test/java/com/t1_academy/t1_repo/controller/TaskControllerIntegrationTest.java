package com.t1_academy.t1_repo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t1_academy.t1_repo.kafka.KafkaTaskProducer;
import com.t1_academy.t1_repo.model.dto.TaskDTO;
import com.t1_academy.t1_repo.model.entity.Task;
import com.t1_academy.t1_repo.model.entity.TaskStatus;
import com.t1_academy.t1_repo.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void createTask_shouldReturnCreated() throws Exception {
        TaskDTO dto = new TaskDTO(null, "Test", "desc", 1L, TaskStatus.NEW);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test"));
    }

    @Test
    void getTaskById_shouldReturnTask() throws Exception {
        Task saved = taskRepository.save(new Task(null, "title", "desc", 1L, TaskStatus.NEW));

        mockMvc.perform(get("/tasks/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"));
    }

    @Test
    void getTaskById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_shouldReturnOk() throws Exception {
        Task task = taskRepository.save(new Task(null, "to delete", "desc", 1L, TaskStatus.NEW));

        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void updateTask_shouldChangeStatusAndReturnOk() throws Exception {
        Task saved = taskRepository.save(new Task(null, "t", "d", 1L, TaskStatus.NEW));
        TaskDTO updateDto = new TaskDTO(saved.getId(), "updated", "d", 1L, TaskStatus.COMPLETED);

        mockMvc.perform(put("/tasks/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        Optional<Task> updated = taskRepository.findById(saved.getId());
        assertTrue(updated.isPresent());
        assertEquals(TaskStatus.COMPLETED, updated.get().getStatus());
    }

    @TestConfiguration
    public class KafkaMockConfig {

        private KafkaTaskProducer kafkaTaskProducer;

        // Можно задать поведение, если требуется:
        @PostConstruct
        public void setUpMockBehavior() {
            doNothing().when(kafkaTaskProducer).sendTo(anyString(), any());
        }
    }
}