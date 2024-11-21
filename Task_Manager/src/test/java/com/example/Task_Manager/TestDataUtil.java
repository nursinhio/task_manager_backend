package com.example.Task_Manager;

import com.example.Task_Manager.model.Task;
import java.util.List;
import java.time.LocalDateTime;

public class TestDataUtil {
    public static Task createTestTask() {
        Task task = new Task();
        task.setTitle("Sample Task");
        task.setDescription("Task Description");
        task.setStatus("TODO");
        task.setPriority("LOW");
        task.setDueDate(LocalDateTime.now());
        return task;
    }

    public static List<Task> createListTask(){
        Task task1 = new Task("Task 1", "Description for task 1", "High", LocalDateTime.now().plusDays(1));
        Task task2 = new Task("Task 2", "Description for task 2", "Medium", LocalDateTime.now().plusDays(2));
        Task task3 = new Task("Task 3", "Description for task 3", "Low", LocalDateTime.now().plusDays(3));

        return List.of(task1, task2, task3);
    }

    // Метод для создания задачи с уникальными данными (например, для обновления)
    public static Task createTestTaskWithCustomData(String name, String description) {
        Task task = new Task();
        task.setTitle(name);
        task.setDescription(description);
        task.setStatus("TODO");
        task.setPriority("HIGH");
        return task;
    }

    // Метод для создания задачи с данным приоритетом
    public static Task createTestTaskWithPriority(String priority) {
        Task task = new Task();
        task.setTitle("Sample Task with Priority");
        task.setDescription("Task Description with custom priority");
        task.setStatus("TODO");
        task.setPriority(priority);
        return task;
    }
}
