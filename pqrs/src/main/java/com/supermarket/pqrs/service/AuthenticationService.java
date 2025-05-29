package com.supermarket.pqrs.service;

import com.supermarket.pqrs.model.*;
import com.supermarket.pqrs.repository.RolRepository;
import com.supermarket.pqrs.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RolRepository rolRepository;
    private final EmailService emailService;

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toList());

        String token = jwtService.generateToken(
                new User(usuario.getUsername(), usuario.getPassword(), authorities)
        );

        String rol = usuario.getRoles().stream()
                .findFirst()
                .map(rolObj -> rolObj.getNombre().name())
                .orElse("SIN_ROL");

        return AuthResponse.builder()
                .mensaje("Login exitoso")
                .token(token)
                .rol(rol)
                .build();
    }

    public AuthResponse register(Usuario usuario) {
        try {
            Set<Rol> rolesPersistidos = usuario.getRoles().stream()
                    .map(r -> {
                        String rolNombreStr = r.getNombre().name();
                        RolNombre rolNombre = RolNombre.valueOf(rolNombreStr.toUpperCase());
                        return rolRepository.findByNombre(rolNombre)
                                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));
                    })
                    .collect(Collectors.toSet());

            usuario.setRoles(rolesPersistidos);

            String rawPassword = usuario.getPassword();
            usuario.setPassword(passwordEncoder.encode(rawPassword));
            usuarioRepository.save(usuario);

            List<GrantedAuthority> authorities = rolesPersistidos.stream()
                    .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                    .collect(Collectors.toList());

            String token = jwtService.generateToken(
                    new User(usuario.getUsername(), usuario.getPassword(), authorities)
            );

            try {
                emailService.enviarCredenciales(usuario.getEmail(), usuario.getUsername(), rawPassword);
            } catch (Exception e) {
                System.err.println("❌ Error al enviar el correo: " + e.getMessage());
            }

            String rol = rolesPersistidos.stream()
                    .findFirst()
                    .map(r -> r.getNombre().name())
                    .orElse("SIN_ROL");

            return AuthResponse.builder()
                    .mensaje("Usuario registrado y correo enviado")
                    .token(token)
                    .rol(rol)
                    .build();

        } catch (Exception e) {
            System.err.println("❌ Error al registrar usuario: " + e.getMessage());

            return AuthResponse.builder()
                    .mensaje("Error al registrar el usuario")
                    .token(null)
                    .rol(null)
                    .build();
        }
    }
}
