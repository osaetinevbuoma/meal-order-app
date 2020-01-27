package com.modnsolutions.project.service;

import com.modnsolutions.project.db.entity.Role;
import com.modnsolutions.project.db.entity.User;
import com.modnsolutions.project.db.repository.RoleRepository;
import com.modnsolutions.project.db.repository.UserRepository;
import com.modnsolutions.project.enums.RoleEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RegistrationService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) return null;

        Role role = roleRepository.findByRole(RoleEnum.ROLE_DEVELOPER.toString()).get();
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(role);
        userRepository.save(user);

        return user;
    }
}
