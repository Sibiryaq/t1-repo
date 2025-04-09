package com.t1_academy.t1_repo.controller;

import com.t1_academy.t1_repo.model.dto.TaskDto;
import com.t1_academy.t1_repo.model.entity.TaskStatus;
import com.t1_academy.t1_repo.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskDto> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public TaskDto createTask(@Valid @RequestBody TaskDto taskDto) {
        return taskService.create(taskDto);
    }

    @PutMapping("{id}")
    public void updateTask(@PathVariable Long id,
                           @RequestParam(required = false) String title,
                           @RequestParam(required = false) String description,
                           @RequestParam(required = false) Long userId,
                           @RequestParam(required = false) TaskStatus status) {
        taskService.update(id, title, description, userId, status);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.delete(id);
    }
}
