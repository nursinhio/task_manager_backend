package com.example.Task_Manager.service;

import com.example.Task_Manager.enums.TaskStatus;
import com.example.Task_Manager.exception.exception.InvalidInputException;
import com.example.Task_Manager.exception.exception.NotFoundException;
import com.example.Task_Manager.model.Task;
import com.example.Task_Manager.model.User;
import com.example.Task_Manager.repository.TaskRepository;
import com.example.Task_Manager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<Task> getAllTask(){
        return taskRepository.findAll();
    }
    public Task createTask(Task task) {
        if (!checkTaskForValidInput(task)) {
            throw new InvalidInputException("Invalid Task Input");
        }
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Task by id " + id + " is not found"));
    }

    public Task deleteById(Long id) {
        Task deletedTask = taskRepository.findById(id)
                .orElseThrow(()->new NotFoundException("No task with id"+ id+ "in database"));
        taskRepository.deleteById(id);
        return deletedTask;
    }

    public Task updateTaskStatusById(Long id, String status){
        Task statusUpdatedTask = taskRepository.findById(id)
                        .orElseThrow(()->new NotFoundException("Task not found"));
        statusUpdatedTask.setStatus(status);
        taskRepository.save(statusUpdatedTask);
        return statusUpdatedTask;
    }
//    приоритет
    public Task updateTaskPriorityById(Long id, String priority){
        Task priorityUpdatedTask = taskRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Task not found"));
        priorityUpdatedTask.setPriority(priority);
        taskRepository.save(priorityUpdatedTask);
        return priorityUpdatedTask;
    }
//    назначать исполнителей задачи
    public Task assignTaskByUserEmail(Long id,String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("User not found"));
        Task task =  taskRepository.findById(id).orElseThrow(()-> new NotFoundException("Task not found"));
        task.setAssignee(user);
        user.getTasks().add(task);
        return taskRepository.save(task);
    }

//    оставлять комментарии.
    public Task leaveCommentByTaskId(Long id, String comment){
        Task task = taskRepository.findById(id).orElseThrow(()->new NotFoundException("Task not found"));
        task.setComment(comment);
        taskRepository.save(task);
        return task;
    }



    private boolean checkTaskForValidInput(Task task) {
        return task.getTitle() != null && !task.getTitle().isEmpty()
                && task.getStatus() != null && !task.getStatus().isEmpty()
                && task.getDueDate() != null;
    }


}