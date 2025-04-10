package com.t1_academy.t1_repo.service;

import com.t1_academy.t1_repo.aspect.annotation.HandlingResult;
import com.t1_academy.t1_repo.aspect.annotation.LogException;
import com.t1_academy.t1_repo.aspect.annotation.LogExecution;
import com.t1_academy.t1_repo.aspect.annotation.LogTracking;
import com.t1_academy.t1_repo.exception.TaskNotFoundException;
import com.t1_academy.t1_repo.mapper.TaskMapper;
import com.t1_academy.t1_repo.model.dto.TaskDTO;
import com.t1_academy.t1_repo.model.entity.Task;
import com.t1_academy.t1_repo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @LogExecution
    public List<TaskDTO> getTasks() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            throw new TaskNotFoundException("Задач нет");
        }
        return tasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    @LogException
    public TaskDTO getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDTO)
                .orElseThrow(() -> new TaskNotFoundException("Задача с id: " + id + " не найдена"));
    }

    @HandlingResult
    public TaskDTO create(TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);
        return taskMapper.toDTO(taskRepository.save(task));
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
    public TaskDTO update(Long id, TaskDTO taskDTO) {
        Task currentTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задачи с id: " + id + " не существует"));
        currentTask.setDescription(taskDTO.getDescription());
        currentTask.setTitle(taskDTO.getTitle());
        currentTask.setUserId(taskDTO.getUserId());
        return taskMapper.toDTO(taskRepository.save(currentTask));
    }

}
