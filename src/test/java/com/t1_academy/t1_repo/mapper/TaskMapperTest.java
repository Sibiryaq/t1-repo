package com.t1_academy.t1_repo.mapper;

import com.t1_academy.t1_repo.model.dto.TaskDTO;
import com.t1_academy.t1_repo.model.entity.Task;
import com.t1_academy.t1_repo.model.entity.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private final TaskMapper mapper = new TaskMapper();

    @Test
    void toDTO_shouldMapCorrectly() {
        Task task = new Task(1L, "title", "desc", 2L, TaskStatus.IN_PROGRESS);

        TaskDTO dto = mapper.toDTO(task);

        assertEquals(task.getId(), dto.getId());
        assertEquals(task.getStatus(), dto.getStatus());
    }

    @Test
    void toEntity_shouldMapCorrectly() {
        TaskDTO dto = new TaskDTO(1L, "title", "desc", 2L, TaskStatus.NEW);

        Task entity = mapper.toEntity(dto);

        assertEquals(dto.getUserId(), entity.getUserId());
        assertEquals(dto.getStatus(), entity.getStatus());
    }
}