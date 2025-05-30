package com.t1_academy.t1_repo.model.dto;

import com.t1_academy.t1_repo.model.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private Long userId;
    private TaskStatus status;
}

