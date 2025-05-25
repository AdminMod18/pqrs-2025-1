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

        return new AuthResponse("Login exitoso", token);
    }

    // Solo para pruebas o inicialización
    public AuthResponse register(Usuario usuario) {
        // ✅ Paso 1: cargar los roles persistidos desde la base de datos
        Set<Rol> rolesPersistidos = usuario.getRoles().stream()
                .map(r -> rolRepository.findByNombre(r.getNombre())
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + r.getNombre())))
                .collect(Collectors.toSet());

        // ✅ Paso 2: reemplazar los roles del usuario con los persistidos
        usuario.setRoles(rolesPersistidos);

        // ✅ Paso 3: codificar contraseña y guardar usuario
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);

        // ✅ Paso 4: crear token con authorities de los roles
        List<GrantedAuthority> authorities = rolesPersistidos.stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toList());

        String token = jwtService.generateToken(
                new User(usuario.getUsername(), usuario.getPassword(), authorities)
        );

        return new AuthResponse("Login exitoso", token);
    }
}
