package com.modnsolutions.project.security;

import com.modnsolutions.project.auth.AuthenticatedUser;
import com.modnsolutions.project.db.entity.Role;
import com.modnsolutions.project.db.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithVendorDetailsSecurityContextFactory implements WithSecurityContextFactory
        <WithMockVendor> {
    @Override
    public SecurityContext createSecurityContext(WithMockVendor annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User vendor = new User(annotation.firstName(), annotation.surname(), annotation.email(),
                new BCryptPasswordEncoder().encode(annotation.password()));
        vendor.setId(annotation.id());
        vendor.setRole(new Role(annotation.role()));

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(vendor);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser,
                authenticatedUser.getPassword(), authenticatedUser.getAuthorities());
        context.setAuthentication(authentication);

        return context;
    }
}
