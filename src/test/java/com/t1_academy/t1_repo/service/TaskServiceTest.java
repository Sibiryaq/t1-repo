package com.t1_academy.t1_repo.service;

import com.t1_academy.t1_repo.exception.TaskNotFoundException;
import com.t1_academy.t1_repo.kafka.KafkaTaskProducer;
import com.t1_academy.t1_repo.mapper.TaskMapper;
import com.t1_academy.t1_repo.model.dto.TaskDTO;
import com.t1_academy.t1_repo.model.entity.Task;
import com.t1_academy.t1_repo.model.entity.TaskStatus;
import com.t1_academy.t1_repo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private KafkaTaskProducer kafkaProducer;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        TaskMapper taskMapper = new TaskMapper();
        taskService = new TaskService(taskRepository, taskMapper, kafkaProducer);
    }

    @Test
    void getTasks_shouldReturnListOfTasks() {
        List<Task> tasks = List.of(new Task(1L, "title", "desc", 1L, TaskStatus.NEW));
        when(taskRepository.findAll()).thenReturn(tasks);

        List<TaskDTO> result = taskService.getTasks();

        assertEquals(1, result.size());
        verify(taskRepository).findAll();
    }

    @Test
    void getTasks_shouldThrowWhenEmpty() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTasks());
    }

    @Test
    void getTaskById_shouldReturnTask() {
        Task task = new Task(1L, "title", "desc", 1L, TaskStatus.NEW);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDTO dto = taskService.getTaskById(1L);

        assertEquals("title", dto.getTitle());
    }

    @Test
    void getTaskById_shouldThrowIfNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void delete_shouldCallRepository() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        taskService.delete(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowIfNotExists() {
        when(taskRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(TaskNotFoundException.class, () -> taskService.delete(1L));
    }
}