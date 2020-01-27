package com.modnsolutions.project.auth;

import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Getter
public class AuthenticatedUser extends User {
    private final com.modnsolutions.project.db.entity.User user;

    public AuthenticatedUser(com.modnsolutions.project.db.entity.User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole()
                .getRole()));
        this.user = user;
    }
}
