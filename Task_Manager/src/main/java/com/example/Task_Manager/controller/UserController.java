package com.example.Task_Manager.controller;

import com.example.Task_Manager.model.User;
import com.example.Task_Manager.service.RoleService;
import com.example.Task_Manager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@RequestMapping("/users")
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.allUsers();
        return users.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(users);
    }

    @GetMapping("/getUserByEmail")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.FOUND);
    }

    @GetMapping("/getLoggedUserEmail")
    public ResponseEntity<User> getLoggedUser(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return ResponseEntity.ok(user);
    }
}
