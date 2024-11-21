package com.example.Task_Manager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String nickname;
    private String email;
    private String password;
}
