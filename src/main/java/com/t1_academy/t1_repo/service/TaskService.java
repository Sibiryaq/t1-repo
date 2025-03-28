package com.t1_academy.t1_repo.service;

import com.t1_academy.t1_repo.exceptions.TaskNotFoundException;
import com.t1_academy.t1_repo.repository.Task;
import com.t1_academy.t1_repo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с id: " + id + " не найдена"));
    }

    public Task create(Task task) {
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Задачи с id: " + id + " не существует");
        }
        taskRepository.deleteById(id);
    }

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
}
