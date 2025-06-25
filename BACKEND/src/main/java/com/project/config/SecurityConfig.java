package com.project.config;

import com.project.config.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .cors(cors -> cors
                    .configurationSource(request -> {
                        var crs = new org.springframework.web.cors.CorsConfiguration();
                        crs.setAllowedOrigins(List.of(
                                "http://localhost:80",
                                "http://localhost:8080",
                                "http://localhost:5000",
                                "http://localhost:5173"
                        ));
                        crs.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                        crs.setAllowedHeaders(List.of("*"));
                        crs.setAllowCredentials(true);
                        return crs;
                    })
            )
            // Wstawiamy filtr JWT przed UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            // Wyłączamy CSRF tylko dla swaggera i endpointów publicznych
            .csrf(csrf -> csrf.disable())
            // Zarządzanie sesją
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Autoryzacja żądań
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/api/v1/stats").permitAll()
                .requestMatchers("/api/v1/whoami").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().permitAll()
            )
            .build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:80", "http://localhost:8080", "http://localhost:5000", "http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

