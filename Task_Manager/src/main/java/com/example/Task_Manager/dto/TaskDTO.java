package com.example.Task_Manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
    private String comment;
    private String assigneeEmail;
}
