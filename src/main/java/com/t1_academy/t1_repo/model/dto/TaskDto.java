package com.t1_academy.t1_repo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Long userId;
}

