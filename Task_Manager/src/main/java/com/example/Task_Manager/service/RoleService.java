package com.example.Task_Manager.service;

import com.example.Task_Manager.model.Role;
import com.example.Task_Manager.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }
    }

    public Role findByName(String name) {
        Optional<Role> role = roleRepository.findByName(name);
        if (role.isPresent()) {
            return role.get();
        } else {
            throw new RuntimeException("Role not found");
        }
    }
}
