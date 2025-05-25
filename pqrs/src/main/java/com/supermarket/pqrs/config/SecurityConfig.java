package com.supermarket.pqrs.config;

import com.supermarket.pqrs.config.JwtAuthenticationFilter;
import com.supermarket.pqrs.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder; // ✅ directamente

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Público: login y registro
                        .requestMatchers("/auth/**").permitAll()

                        // BLOQUEOS: Bloquear métodos específicos para /api/roles/**
                        .requestMatchers(HttpMethod.POST, "/api/roles/**").denyAll()
                        .requestMatchers(HttpMethod.PUT, "/api/roles/**").denyAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/roles/**").denyAll()

                        // BLOQUEOS: Bloquear DELETE para usuarios y clientes también
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").denyAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/clientes/**").denyAll()

                        //  Accesos solo para GESTOR (excepto los bloqueados arriba)
                        .requestMatchers("/api/usuarios/**", "/api/roles/**").hasRole("GESTOR")

                        //  Accesos para GESTOR o CLIENTE
                        .requestMatchers("/api/clientes/**", "/api/radicados/**", "/api/anexos/**")
                        .hasAnyRole("GESTOR", "CLIENTE")

                        // ✅ Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder); // ✅ cambio aquí
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
