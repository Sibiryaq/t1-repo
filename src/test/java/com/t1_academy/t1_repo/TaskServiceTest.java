package com.t1_academy.t1_repo;

import com.t1_academy.t1_repo.exception.TaskNotFoundException;
import com.t1_academy.t1_repo.model.entity.Task;
import com.t1_academy.t1_repo.repository.TaskRepository;
import com.t1_academy.t1_repo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task(1L, "Название", "Описание", 100L);
    }

    @Test
    void shouldReturnAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(testTask));

        List<Task> tasks = taskService.getTasks();

        assertEquals(1, tasks.size());
        assertEquals("Название", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        Task foundTask = taskService.getTaskById(1L);

        assertNotNull(foundTask);
        assertEquals(1L, foundTask.getId());
        assertEquals("Название", foundTask.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(2L));

        assertEquals("Задача с id: 2 не найдена", exception.getMessage());
        verify(taskRepository, times(1)).findById(2L);
    }

    @Test
    void shouldCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task createdTask = taskService.create(testTask);

        assertNotNull(createdTask);
        assertEquals("Название", createdTask.getTitle());
        verify(taskRepository, times(1)).save(testTask);
    }

    @Test
    void shouldDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        assertDoesNotThrow(() -> taskService.delete(1L));
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentTask() {
        when(taskRepository.existsById(2L)).thenReturn(false);

        Exception exception = assertThrows(TaskNotFoundException.class, () -> taskService.delete(2L));

        assertEquals("Задачи с id: 2 не существует", exception.getMessage());
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldUpdateTask() {
        String newTitle = "Новое название";
        String newDescription = "Новое описание";
        Long newUserId = 200L;

        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        assertDoesNotThrow(() -> taskService.update(1L, newTitle, newDescription, newUserId));

        assertEquals(newTitle, testTask.getTitle());
        assertEquals(newDescription, testTask.getDescription());
        assertEquals(newUserId, testTask.getUserId());

        verify(taskRepository, times(1)).save(testTask);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentTask() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class,
                () -> taskService.update(2L, "Новое название", "Новое описание", 300L));

        assertEquals("Задачи с id: 2 не существует", exception.getMessage());

        verify(taskRepository, never()).save(any(Task.class));
    }
}
