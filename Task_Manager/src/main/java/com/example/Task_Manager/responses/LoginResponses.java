package com.example.Task_Manager.responses;

import com.example.Task_Manager.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LoginResponses {
    private String token;
    private long expiresIn;
    private String role;
}
