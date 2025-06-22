package com.anycart.anycart.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private LoggingFilter loggingFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/v1/products/**", "/api/v1/categories/viewAll",
                                "/api/v1/categories/viewSubcategory/**", "/api/v1/categories/viewById/**")
                        .permitAll()
                        .requestMatchers("/api/v1/categories/add", "/api/v1/categories/update/**",
                                "/api/v1/categories/remove/**")
                        .hasRole("Admin")
                        .requestMatchers("/api/v1/cartItems/**").authenticated()
                        .requestMatchers("/api/v1/orders/update/**", "/api/v1/orders/all").hasRole("Admin")
                        .requestMatchers("/api/v1/cartItems/**",
                                "/api/v1/reviews",
                                "/api/v1/reviews/**")
                        .authenticated()
                        .requestMatchers("/api/v1/orders/place", "/api/v1/orders/view/**",
                                "/api/v1/orders/history", "/api/v1/orders/cancel/**")
                        .authenticated()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}