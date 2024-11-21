package com.example.Task_Manager.repository;

import com.example.Task_Manager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
