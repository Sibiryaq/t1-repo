package com.t1_academy.t1_repo.service;

import com.t1_academy.t1_repo.aspect.annotation.HandlingResult;
import com.t1_academy.t1_repo.aspect.annotation.LogException;
import com.t1_academy.t1_repo.aspect.annotation.LogExecution;
import com.t1_academy.t1_repo.aspect.annotation.LogTracking;
import com.t1_academy.t1_repo.exception.TaskNotFoundException;
import com.t1_academy.t1_repo.model.dto.TaskDto;
import com.t1_academy.t1_repo.model.entity.Task;
import com.t1_academy.t1_repo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @LogExecution
    public List<TaskDto> getTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @LogException
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с id: " + id + " не найдена"));
        return convertToDto(task);
    }

    @HandlingResult
    public TaskDto create(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setUserId(taskDto.getUserId());
        return convertToDto(taskRepository.save(task));
    }

    @LogExecution
    @LogException
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Задачи с id: " + id + " не существует");
        }
        taskRepository.deleteById(id);
    }

    @LogTracking
    public void update(Long id, String title, String description, Long userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задачи с id: " + id + " не существует"));

        if (title != null) {
            task.setTitle(title);
        }

        if (description != null) {
            task.setDescription(description);
        }

        if (userId != null) {
            task.setUserId(userId);
        }

        taskRepository.save(task);
    }

    private TaskDto convertToDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getUserId());
    }
}
