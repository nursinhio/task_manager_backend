package com.example.Task_Manager.model;
import com.example.Task_Manager.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name="tasks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    private String description;
    @Column(nullable = false)
    private String status = TaskStatus.NEW.name();
    @Column(nullable = false)
    private String priority;
    @Column(nullable = false)
    private LocalDateTime dueDate;
    private String comment;

    public Task(String title, String description, String priority, LocalDateTime dueDate){
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = TaskStatus.NEW.name();
    }

    @JsonIgnore
    private boolean isFinished;
    @JsonIgnore
    private boolean isStarted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User assignee;
}
