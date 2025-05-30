package com.supermarket.pqrs.service;

import com.supermarket.pqrs.model.*;
import com.supermarket.pqrs.repository.RolRepository;
import com.supermarket.pqrs.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_deberiaRetornarTokenYRolSiElUsuarioExiste() {
        AuthRequest request = new AuthRequest("testuser", "password");
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setPassword("encodedPassword");

        Rol rol = new Rol(1L, RolNombre.CLIENTE);
        usuario.setRoles(Set.of(rol));

        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthResponse response = authenticationService.login(request);

        assertEquals("Login exitoso", response.getMensaje());
        assertEquals("jwt-token", response.getToken());
        assertEquals("CLIENTE", response.getRol());

        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void register_deberiaGuardarUsuarioYRetornarTokenYRol() {
        Usuario usuario = new Usuario();
        usuario.setUsername("nuevo");
        usuario.setPassword("1234");
        usuario.setEmail("nuevo@email.com");

        Rol rol = new Rol(1L, RolNombre.CLIENTE);
        usuario.setRoles(Set.of(rol));

        when(rolRepository.findByNombre(RolNombre.CLIENTE)).thenReturn(Optional.of(rol));
        when(passwordEncoder.encode("1234")).thenReturn("encodedPass");
        when(usuarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthResponse response = authenticationService.register(usuario);

        assertEquals("Usuario registrado y correo enviado", response.getMensaje());
        assertEquals("jwt-token", response.getToken());
        assertEquals("CLIENTE", response.getRol());

        verify(usuarioRepository).save(any(Usuario.class));
        verify(jwtService).generateToken(any(User.class));
        verify(emailService).enviarCredenciales("nuevo@email.com", "nuevo", "1234");
    }

    @Test
    void login_deberiaLanzarExcepcionSiUsuarioNoExiste() {
        AuthRequest request = new AuthRequest("invalido", "1234");

        when(usuarioRepository.findByUsername("invalido")).thenReturn(Optional.empty());

        // Simular que pasa la autenticación pero no se encuentra en base de datos
        assertThrows(RuntimeException.class, () -> authenticationService.login(request));
    }

    @Test
    void register_deberiaRetornarErrorSiRolNoExiste() {
        Usuario usuario = new Usuario();
        usuario.setUsername("nuevo");
        usuario.setPassword("1234");
        usuario.setEmail("nuevo@email.com");

        Rol rol = new Rol(1L, RolNombre.GESTOR);
        usuario.setRoles(Set.of(rol));

        when(rolRepository.findByNombre(RolNombre.GESTOR)).thenReturn(Optional.empty());

        AuthResponse response = authenticationService.register(usuario);

        assertEquals("Error al registrar el usuario", response.getMensaje());
        assertNull(response.getToken());
        assertNull(response.getRol());
    }
}
