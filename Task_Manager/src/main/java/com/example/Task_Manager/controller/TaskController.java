package com.example.Task_Manager.controller;

import com.example.Task_Manager.dto.TaskDTO;
import com.example.Task_Manager.enums.TaskStatus;
import com.example.Task_Manager.mapper.TaskMapper;
import com.example.Task_Manager.model.Task;
import com.example.Task_Manager.model.User;
import com.example.Task_Manager.service.TaskService;
import com.example.Task_Manager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final UserService userService;


    @GetMapping("/getTaskDetails/{id}")
    public ResponseEntity<TaskDTO> getTaskDetails(@PathVariable Long id, Principal principal) {
        Task task = userService.findUserTaskByTaskId(id, principal.getName());
        return new ResponseEntity<>(taskMapper.mapTo(task), HttpStatus.OK);
    }

    @GetMapping("/getUserTasks")
    public ResponseEntity<List<Task>> getUserTasks(Principal principal){
        return new ResponseEntity<>(userService.getUserTasks(principal.getName()), HttpStatus.OK);
    }

    @PutMapping("/updateTaskStatusToInProcess")
    public ResponseEntity<TaskDTO> updateTaskStatusToInProcessById(@RequestParam Long id, Principal principal){
        Task task = userService.findUserTaskByTaskId(id, principal.getName());
        Task updated =taskService.updateTaskStatusById(task.getId(), TaskStatus.IN_PROCESS.name());
        return new ResponseEntity<>(taskMapper.mapTo(updated), HttpStatus.OK);
    }

    @PutMapping("/updateTaskStatusToFinished")
    public ResponseEntity<TaskDTO> updateTaskStatusToFinishedById(@RequestParam Long id, Principal principal){
        Task task = userService.findUserTaskByTaskId(id, principal.getName());
        Task updated =taskService.updateTaskStatusById(task.getId(), TaskStatus.FINISHED.name());
        return new ResponseEntity<>(taskMapper.mapTo(updated), HttpStatus.OK);
    }


    @PutMapping("/leaveCommentaryByTaskId")
    public ResponseEntity<TaskDTO> leaveCommentByTaskId(@RequestParam Long id,@RequestParam String comment, Principal principal){
        Task task = userService.findUserTaskByTaskId(id, principal.getName());
        Task updated = taskService.leaveCommentByTaskId(task.getId(), comment);
        return new ResponseEntity<>(taskMapper.mapTo(updated), HttpStatus.OK);
    }
}
