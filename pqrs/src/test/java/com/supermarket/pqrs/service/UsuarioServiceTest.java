package com.supermarket.pqrs.service;

import com.supermarket.pqrs.model.Rol;
import com.supermarket.pqrs.model.RolNombre;
import com.supermarket.pqrs.model.Usuario;
import com.supermarket.pqrs.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_deberiaRetornarListaDeUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(new Usuario()));

        List<Usuario> result = usuarioService.findAll();

        assertEquals(1, result.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void findById_deberiaRetornarUsuarioSiExiste() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void save_deberiaEncriptarYGuardarUsuarioConPasswordPlano() {
        Usuario usuario = new Usuario();
        usuario.setPassword("123456");

        when(passwordEncoder.encode("123456")).thenReturn("encoded123");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario saved = usuarioService.save(usuario);

        assertEquals("encoded123", saved.getPassword());
        verify(passwordEncoder).encode("123456");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void save_deberiaGuardarPasswordYaEncriptada() {
        Usuario usuario = new Usuario();
        usuario.setPassword("$2a$10$somethingencrypted");

        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario saved = usuarioService.save(usuario);

        assertEquals("$2a$10$somethingencrypted", saved.getPassword());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void delete_deberiaEliminarUsuarioPorId() {
        usuarioService.delete(1L);
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void crearNuevoUsuario_deberiaCrearYGuardarUsuarioConRolesYPasswordEncriptado() {
        Set<Rol> roles = Set.of(new Rol(1L, RolNombre.CLIENTE));
        when(passwordEncoder.encode("abc123")).thenReturn("encodedABC");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario nuevo = usuarioService.crearNuevoUsuario("cliente1", "abc123", roles);

        assertEquals("cliente1", nuevo.getUsername());
        assertEquals("encodedABC", nuevo.getPassword());
        assertEquals(roles, nuevo.getRoles());

        verify(usuarioRepository).save(nuevo);
    }
}
