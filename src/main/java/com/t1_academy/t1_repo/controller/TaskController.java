package com.t1_academy.t1_repo.controller;

import com.t1_academy.t1_repo.model.dto.TaskDTO;
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
    public List<TaskDTO> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public TaskDTO createTask(@Valid @RequestBody TaskDTO taskDto) {
        return taskService.create(taskDto);
    }

    @PutMapping("{id}")
    public void updateTask(@PathVariable Long id,
                           @RequestBody TaskDTO taskDTO) {
        taskService.update(id, taskDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.delete(id);
    }
}
