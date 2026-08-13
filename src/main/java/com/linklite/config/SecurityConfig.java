package com.linklite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Stateless REST API - no CSRF tokens needed since we don't use cookie sessions
            .csrf(csrf -> csrf.disable())

            // No HTTP sessions - every request is independent
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // IMPORTANT: without this, spring-boot-starter-security auto-locks
            // every endpoint behind a login form / random generated password,
            // which breaks /api/auth, /api/urls, and the public redirect
            // endpoint entirely. This keeps the API open; CORS in WebConfig
            // still restricts which origins can call it.
            // TODO: once you wire up JWT (the jjwt deps in pom.xml are unused
            // right now), swap the line below for real per-endpoint rules and
            // add a JwtAuthFilter before UsernamePasswordAuthenticationFilter.
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

            // Disable the default login page / HTTP Basic prompt
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable());

        return http.build();
    }
}
