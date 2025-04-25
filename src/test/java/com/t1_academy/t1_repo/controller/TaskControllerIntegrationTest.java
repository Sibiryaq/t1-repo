package com.t1_academy.t1_repo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t1_academy.t1_repo.config.TestContainersConfig;
import com.t1_academy.t1_repo.model.dto.TaskDTO;
import com.t1_academy.t1_repo.model.entity.Task;
import com.t1_academy.t1_repo.model.entity.TaskStatus;
import com.t1_academy.t1_repo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerIntegrationTest extends TestContainersConfig {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("Создание задачи")
    void createTask_shouldReturnCreated() throws Exception {
        TaskDTO dto = new TaskDTO(null, "Test", "desc", 1L, TaskStatus.NEW);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test"));
    }

    @Test
    @DisplayName("Получение задачи по ID")
    void getTaskById_shouldReturnTask() throws Exception {
        Task saved = taskRepository.save(new Task(null, "title", "desc", 1L, TaskStatus.NEW));

        mockMvc.perform(get("/tasks/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"));
    }

    @Test
    @DisplayName("Получение несуществующей задачи")
    void getTaskById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Удаление задачи")
    void deleteTask_shouldReturnOk() throws Exception {
        Task task = taskRepository.save(new Task(null, "to delete", "desc", 1L, TaskStatus.NEW));

        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Обновление задачи: должен изменить статус и вернуть 200")
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

}