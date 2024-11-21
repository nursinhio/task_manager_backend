package com.example.Task_Manager.mapper;

import com.example.Task_Manager.dto.TaskDTO;
import com.example.Task_Manager.exception.exception.NotFoundException;
import com.example.Task_Manager.model.Task;
import com.example.Task_Manager.model.User;
import com.example.Task_Manager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

@Component
@AllArgsConstructor
public class TaskMapper implements Mapper<Task, TaskDTO> {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    @Override
    public TaskDTO mapTo(Task task) {
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);

        if (task.getAssignee() != null) {
            taskDTO.setAssigneeEmail(task.getAssignee().getEmail());
        }

        return taskDTO;
    }

    @Override
    public Task mapFrom(TaskDTO taskDTO) {
        Task task = modelMapper.map(taskDTO, Task.class);

        if (taskDTO.getAssigneeEmail() != null) {
            User user = userRepository.findByEmail(taskDTO.getAssigneeEmail())
                    .orElseThrow(() -> new NotFoundException("User not found with email: " + taskDTO.getAssigneeEmail()));
            task.setAssignee(user);
        }

        return task;
    }
}
