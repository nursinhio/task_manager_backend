package com.example.Task_Manager.controller;

import com.example.Task_Manager.dto.TaskDTO;
import com.example.Task_Manager.mapper.TaskMapper;
import com.example.Task_Manager.model.Task;
import com.example.Task_Manager.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tasks")
@AllArgsConstructor
public class AdminTaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping("/")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTask().stream().map(taskMapper::mapTo).toList(), HttpStatus.FOUND);
    }

    @PostMapping("/createTask")
    public ResponseEntity<?> createTask(@RequestBody TaskDTO taskDto) {
        Task task = taskMapper.mapFrom(taskDto);
        System.out.println(task);
        Task created = taskService.createTask(task);
        return new ResponseEntity<>(taskMapper.mapTo(created), HttpStatus.CREATED);

    }

    @DeleteMapping("/deleteTaskById/{id}")
    public ResponseEntity<TaskDTO> deleteTaskById(@PathVariable("id") Long id) {
        Task deletedTask = taskService.deleteById(id);
        return new ResponseEntity<>(taskMapper.mapTo(deletedTask), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/updateTaskStatusById/{id}")
    public ResponseEntity<TaskDTO> updateTaskStatusById(@PathVariable("id") Long id, @RequestParam String status) {
        Task task = taskService.updateTaskStatusById(id, status);
        return new ResponseEntity<>(taskMapper.mapTo(task), HttpStatus.OK);
    }

    @PutMapping("/updateTaskPriorityById/{id}")
    public ResponseEntity<TaskDTO> updateTaskPriorityById(@PathVariable("id") Long id, @RequestParam String priority) {
        Task task = taskService.updateTaskPriorityById(id, priority);
        return new ResponseEntity<>(taskMapper.mapTo(task), HttpStatus.OK);
    }

    @PutMapping("/assignTaskByUserEmail")
    public ResponseEntity<TaskDTO> assignTaskByUserEmail(@RequestParam Long id, @RequestParam String email) {
        Task assignedTask = taskService.assignTaskByUserEmail(id, email);
        return new ResponseEntity<>(taskMapper.mapTo(assignedTask), HttpStatus.OK);
    }

    @PutMapping("/leaveCommentaryByTaskId/{id}")
    public ResponseEntity<TaskDTO> leaveCommentByTaskId(@PathVariable("id") Long id, @RequestParam String comment) {
        Task commentedTask = taskService.leaveCommentByTaskId(id, comment);
        return new ResponseEntity<>(taskMapper.mapTo(commentedTask), HttpStatus.OK);
    }
}
