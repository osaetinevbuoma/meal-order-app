package com.modnsolutions.project.service;

import com.modnsolutions.project.auth.AuthenticatedUser;
import com.modnsolutions.project.db.entity.User;
import com.modnsolutions.project.db.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(AuthenticatedUser::new).orElse(null);
    }

    /**
     * Returns authentication context
     * @return
     */
    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Returns an authenticated user
     * @return
     */
    public AuthenticatedUser getAuthenticatedUser() {
        return (AuthenticatedUser) getAuth().getPrincipal();
    }
}
