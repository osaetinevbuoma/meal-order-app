package ng.com.byteworks.project.service;

import ng.com.byteworks.project.enums.RoleEnum;
import ng.com.byteworks.project.db.entity.Role;
import ng.com.byteworks.project.db.entity.User;
import ng.com.byteworks.project.db.repository.RoleRepository;
import ng.com.byteworks.project.db.repository.UserRepository;
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
