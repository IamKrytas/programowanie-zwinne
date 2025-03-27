package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Configures security settings for HTTP requests.
     * This method defines access rules for various endpoints, such as public routes and restricted routes.
     */
    @SuppressWarnings("removal")
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            // Disable CSRF for /public/** endpoints (no CSRF protection for these)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/public/**"))

            // Configures session management
            .sessionManagement()
            .sessionFixation().migrateSession() // Migrate session after login to avoid session fixation
            .maximumSessions(1)  // Limit the number of active sessions per user
            .expiredUrl("/login?expired")  // Redirect to login page if session expires
            .and()
            .and()

            // Configures authorization rules for HTTP requests using authorizeHttpRequests
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()  // Allow access to public endpoints without authentication
                .requestMatchers("/login").permitAll()  // Allow access to the login page without authentication
                .requestMatchers("/admin/**").hasRole("ADMIN")  // Only users with ADMIN role can access /admin/**
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")  // Only users with USER or ADMIN role can access /user/**
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()  // All other requests require authentication
            )

            // Configures login with default login page
            .formLogin(form -> form
                .permitAll()  // Allow everyone to access the login page
            )
            
            .csrf(csrf -> csrf.ignoringRequestMatchers("/swagger-ui/**", "/v3/api-docs/**"))

            // Configure logout
            .logout(logout -> logout
                .logoutUrl("/logout")  // URL to trigger logout
                .logoutSuccessUrl("/login?logout")  // Redirect to login page after logout
                .permitAll()  // Allow everyone to access the logout functionality
            );

        return httpSecurity.build();  // Builds and returns the security configuration
    }

    /**
     * Defines a fake UserDetailsService that always returns the same users (admin and user).
     * This method simulates user authentication for testing.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if (null == username) {
                throw new RuntimeException("User not found");  // Throws an exception if the user is not found
            } else switch (username) {
                case "admin" -> {
                    // Returns a user with username "admin", role "ADMIN" and password "admin123"
                    return User.withUsername("admin")
                            .password(passwordEncoder().encode("admin123"))
                            .roles("ADMIN")
                            .build();
                }
                case "user" -> {
                    // Returns a user with username "user", role "USER" and password "user123"
                    return User.withUsername("user")
                            .password(passwordEncoder().encode("user123"))
                            .roles("USER")
                            .build();
                }
                default -> throw new RuntimeException("User not found");  // Throws an exception if the user is not found
            }
        };
    }

    /**
     * Configures the password encoder using BCrypt.
     * This encoder is used to securely hash passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Uses BCrypt for secure password hashing
    }
}
