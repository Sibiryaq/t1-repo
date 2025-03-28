package com.t1_academy.t1_repo.repository;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название задачи не должно быть пустым")
    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "user_id", nullable = false)
    private Long userId;


}
