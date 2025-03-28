package com.t1_academy.t1_repo.service;

import com.t1_academy.t1_repo.exceptions.TaskNotFoundException;
import com.t1_academy.t1_repo.repository.Task;
import com.t1_academy.t1_repo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.orElse(null);
    }

    public Task create(Task task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Описание не должно быть пустым");
        }
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

        if (title != null && !title.equals(task.getTitle())) {
            task.setTitle(title);
        }

        if (description != null && !description.equals(task.getDescription())) {
            task.setDescription(description);
        }

        if (userId != null && !userId.equals(task.getUserId())) {
            task.setUserId(userId);
        }

        taskRepository.save(task);
    }
}
