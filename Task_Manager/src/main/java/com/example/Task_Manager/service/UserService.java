package com.example.Task_Manager.service;

import com.example.Task_Manager.exception.exception.NotFoundException;
import com.example.Task_Manager.model.Task;
import com.example.Task_Manager.model.User;
import com.example.Task_Manager.repository.TaskRepository;
import com.example.Task_Manager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public List<Task> getUserTasks(String username){
        User user = userRepository.findByEmail(username).orElseThrow(()->new NotFoundException("User not found"));
        return user.getTasks();
    }

    public Task findUserTaskByTaskId(Long id, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("There is no user with such email"));

        return user.getTasks().stream().filter(t -> Objects.equals(t.getId(), id)).findFirst()
                .orElseThrow(()-> new NotFoundException("Task is not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("No user with email " + email));
    }
}
