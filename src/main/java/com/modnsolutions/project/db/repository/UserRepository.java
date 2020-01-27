package com.modnsolutions.project.db.repository;

import com.modnsolutions.project.db.entity.Role;
import com.modnsolutions.project.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRole(Role role);
}
