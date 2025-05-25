package com.supermarket.pqrs.service;

import com.supermarket.pqrs.model.Rol;
import com.supermarket.pqrs.model.RolNombre;
import com.supermarket.pqrs.model.Usuario;
import com.supermarket.pqrs.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_deberiaRetornarUserDetailsConRoles() {
        Rol rol = new Rol(1L, RolNombre.GESTOR);
        Usuario usuario = new Usuario();
        usuario.setUsername("gestor");
        usuario.setPassword("secret");
        usuario.setRoles(Set.of(rol));

        when(usuarioRepository.findByUsername("gestor")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("gestor");

        assertNotNull(userDetails);
        assertEquals("gestor", userDetails.getUsername());
        assertEquals("secret", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_GESTOR")));
    }

    @Test
    void loadUserByUsername_deberiaLanzarExcepcionSiNoExiste() {
        when(usuarioRepository.findByUsername("inexistente")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("inexistente");
        });
    }
}
