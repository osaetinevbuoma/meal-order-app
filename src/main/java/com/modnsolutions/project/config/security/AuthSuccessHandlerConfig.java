package com.modnsolutions.project.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class AuthSuccessHandlerConfig implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_VENDOR")) {
                if (null == savedRequest)
                    redirectStrategy.sendRedirect(request, response, "/vendor/meals");
                else redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
            }

            if (authority.getAuthority().equals("ROLE_DEVELOPER")) {
                if (null == savedRequest)
                    redirectStrategy.sendRedirect(request, response, "/meals");
                else redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
            }
        }
    }
}
