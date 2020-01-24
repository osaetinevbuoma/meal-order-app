package ng.com.byteworks.project.config.security;

import ng.com.byteworks.project.enums.RoleEnum;
import ng.com.byteworks.project.service.AuthenticationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationService authenticationService;
    private final AuthSuccessHandlerConfig authSuccessHandlerConfig;

    public SecurityConfig(AuthenticationService authenticationService,
                          AuthSuccessHandlerConfig authSuccessHandlerConfig) {
        this.authenticationService = authenticationService;
        this.authSuccessHandlerConfig = authSuccessHandlerConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/javascripts/**", "/stylesheets/**", "/images/**",
                        "/fonts/**", "/api/developer/register").permitAll()
                .antMatchers("/vendor/**").hasAuthority(RoleEnum.ROLE_VENDOR.toString())
                .antMatchers("/api/vendor/**").hasAuthority(RoleEnum.ROLE_VENDOR.toString())
                .antMatchers("/api/**").hasAuthority(RoleEnum.ROLE_DEVELOPER.toString())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/").permitAll()
                    .failureUrl("/?auth-failed=true")
                    .usernameParameter("email")
                    .successHandler(authSuccessHandlerConfig)
                .and()
                .logout().permitAll()
                    .logoutUrl("/logout");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
