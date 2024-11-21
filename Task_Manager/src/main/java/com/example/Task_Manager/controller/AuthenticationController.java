package com.example.Task_Manager.controller;

import com.example.Task_Manager.dto.LoginUserDto;
import com.example.Task_Manager.dto.RegisterUserDto;
import com.example.Task_Manager.dto.VerifyUserDto;
import com.example.Task_Manager.model.User;
import com.example.Task_Manager.responses.LoginResponses;
import com.example.Task_Manager.service.AuthenticationService;
import com.example.Task_Manager.service.JwtService;
import com.example.Task_Manager.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final RoleService roleService;

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestParam String nickname,
                                         @RequestParam String email,
                                         @RequestParam String password) {
        try {
            User adminUser = authenticationService.createAdmin(nickname, email, password);
            return ResponseEntity.ok(adminUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }



    @PostMapping(path = "/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
        try {
            User registerUser = authenticationService.singUp(registerUserDto);
            return ResponseEntity.ok(registerUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            User authenticateUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticateUser);
            LoginResponses loginResponse = new LoginResponses(jwtToken, jwtService.getExpirationTime(),authenticateUser.getRoles().getFirst().getName() );
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            User verifiedUser= authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok(verifiedUser);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok(("verification code sent"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
