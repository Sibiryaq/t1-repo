package com.t1_academy.t1_repo.mapper;

import com.t1_academy.t1_repo.model.dto.TaskDTO;
import com.t1_academy.t1_repo.model.dto.TaskStatusUpdateDTO;
import com.t1_academy.t1_repo.model.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDTO toDTO(Task task) {
        if (task == null) {
            return null;
        }

        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getUserId(),
                task.getStatus()
        );
    }

    public Task toEntity(TaskDTO dto) {
        if (dto == null) {
            return null;
        }

        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setUserId(dto.getUserId());
        task.setStatus(dto.getStatus());
        return task;
    }

    public TaskStatusUpdateDTO toStatusUpdateDTO(Task task) {
        if (task == null) {
            return null;
        }

        return new TaskStatusUpdateDTO(
                task.getId(),
                task.getStatus().name()
        );
    }
}
