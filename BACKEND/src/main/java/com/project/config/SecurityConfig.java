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
            // Łączymy wszystkie wykluczenia CSRF w jednym miejscu
            .csrf(csrf -> csrf.ignoringRequestMatchers("/public/**", "/swagger-ui/**", "/v3/api-docs/**"))

            .sessionManagement()
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .expiredUrl("/login?expired")
                .and()
            .and()

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )

            .formLogin(form -> form.permitAll())

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return httpSecurity.build();
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
