package com.example.Task_Manager;

import com.example.Task_Manager.model.Task;
import com.example.Task_Manager.model.User;
import com.example.Task_Manager.service.TaskService;
import com.example.Task_Manager.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public TaskControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper, TaskService taskService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.taskService = taskService;
    }

    @Test
    public void TestThatCreateTaskReturnsWith201Created() throws Exception {
        Task task = TestDataUtil.createTestTask();
        task.setId(null);
        String json = objectMapper.writeValueAsString(task);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/tasks/createTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").password("adminPassword").roles("ADMIN"))
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
