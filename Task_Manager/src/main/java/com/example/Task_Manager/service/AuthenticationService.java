package com.example.Task_Manager.service;

import com.example.Task_Manager.dto.LoginUserDto;
import com.example.Task_Manager.dto.RegisterUserDto;
import com.example.Task_Manager.dto.VerifyUserDto;
import com.example.Task_Manager.model.Role;
import com.example.Task_Manager.model.User;
import com.example.Task_Manager.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.List;



@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    private final RoleService roleService;

    public User singUp(RegisterUserDto input){
        Role userRole = roleService.findByName("ROLE_USER");
        User user = new User(input.getNickname(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiredAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(true);
        user.setRoles(List.of(userRole));
        User saved = userRepository.save(user);
        sendVerificationEmail(user);
        return saved;
    }

    public User createAdmin(String nickname, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with this email already exists!");
        }
        User adminUser = new User(nickname, email, passwordEncoder.encode(password));
        adminUser.setEnabled(true);
        Role adminRole = roleService.findByName("ROLE_ADMIN");
        adminUser.setRoles(List.of(adminRole));
        return userRepository.save(adminUser);
    }

    public User authenticate(LoginUserDto input){
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(!user.isEnabled()){
            throw new RuntimeException("Account not verified");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(), input.getPassword()
                )
        );
        return user;
    }

    public User verifyUser(VerifyUserDto input){
        Optional<User> optional = userRepository.findByEmail(input.getEmail());
        if(optional.isPresent()){
            User user = optional.get();
            if(user.getVerificationCodeExpiredAt().isBefore(LocalDateTime.now())){
                throw new RuntimeException("Verification code has expired");
            }

            if(user.getVerificationCode().equals(input.getVerificationCode())){
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiredAt(null);
                return userRepository.save(user);
            }else{
                throw new RuntimeException("Invalid verification code");
            }
        }else{
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email){
        Optional<User> optional = userRepository.findByEmail(email);
        if(optional.isPresent()){
            User user = optional.get();
            if(user.isEnabled()){
                throw new RuntimeException("User already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiredAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        }else{
            throw new RuntimeException("User not found");
        }
    }

    public void sendVerificationEmail(User user){
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try{
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        }catch (MessagingException exception){
            exception.printStackTrace();
        }
    }

    private String generateVerificationCode(){
        Random random = new Random();
        int code = random.nextInt(900000)+100000;
        return String.valueOf(code);
    }
}
