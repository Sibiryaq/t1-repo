package com.t1_academy.t1_repo.service;

import com.t1_academy.t1_repo.aspect.annotation.HandlingResult;
import com.t1_academy.t1_repo.aspect.annotation.LogException;
import com.t1_academy.t1_repo.aspect.annotation.LogExecution;
import com.t1_academy.t1_repo.aspect.annotation.LogTracking;
import com.t1_academy.t1_repo.exception.TaskNotFoundException;
import com.t1_academy.t1_repo.kafka.KafkaTaskProducer;
import com.t1_academy.t1_repo.mapper.TaskMapper;
import com.t1_academy.t1_repo.model.dto.TaskDTO;
import com.t1_academy.t1_repo.model.dto.TaskStatusUpdateDTO;
import com.t1_academy.t1_repo.model.entity.Task;
import com.t1_academy.t1_repo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final KafkaTaskProducer kafkaProducer;

    @Value("${task.kafka.topic.status-updated}")
    private String kafkaTopic;

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
    @Transactional
    public TaskDTO update(Long id, TaskDTO taskDTO) {
        Task currentTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задачи с id: " + id + " не существует"));
        String oldStatus = currentTask.getStatus().name();
        currentTask.setDescription(taskDTO.getDescription());
        currentTask.setTitle(taskDTO.getTitle());
        currentTask.setUserId(taskDTO.getUserId());
        currentTask.setStatus(taskDTO.getStatus());

        Task updated = taskRepository.save(currentTask);

        TaskStatusUpdateDTO kafkaDTO = taskMapper.toStatusUpdateDTO(updated);
        kafkaProducer.sendTo(kafkaTopic, kafkaDTO);
        return taskMapper.toDTO(updated);
    }

}
