package ng.com.byteworks.project.security;

import ng.com.byteworks.project.auth.AuthenticatedUser;
import ng.com.byteworks.project.db.entity.Role;
import ng.com.byteworks.project.db.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithDeveloperDetailsSecurityContextFactory implements WithSecurityContextFactory
        <WithMockDeveloper> {
    @Override
    public SecurityContext createSecurityContext(WithMockDeveloper annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User developer = new User(annotation.firstName(), annotation.surname(), annotation.email(),
                new BCryptPasswordEncoder().encode(annotation.password()));
        developer.setId(annotation.id());
        developer.setRole(new Role(annotation.role()));

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(developer);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser,
                authenticatedUser.getPassword(), authenticatedUser.getAuthorities());
        context.setAuthentication(authentication);

        return context;
    }
}
