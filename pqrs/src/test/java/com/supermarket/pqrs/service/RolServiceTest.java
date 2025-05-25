package com.supermarket.pqrs.service;

import com.supermarket.pqrs.exception.ResourceNotFoundException;
import com.supermarket.pqrs.model.Rol;
import com.supermarket.pqrs.model.RolNombre;
import com.supermarket.pqrs.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_deberiaRetornarListaDeRoles() {
        when(rolRepository.findAll()).thenReturn(List.of(new Rol()));

        List<Rol> resultado = rolService.findAll();

        assertEquals(1, resultado.size());
        verify(rolRepository, times(1)).findAll();
    }

    @Test
    void findById_deberiaRetornarRolSiExiste() {
        Rol rol = new Rol(1L, RolNombre.CLIENTE);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        Optional<Rol> resultado = rolService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void save_deberiaGuardarRolNuevo() {
        Rol rol = new Rol(null, RolNombre.GESTOR);

        when(rolRepository.findByNombre(RolNombre.GESTOR)).thenReturn(Optional.empty());
        when(rolRepository.save(rol)).thenReturn(rol);

        Rol resultado = rolService.save(rol);

        assertNotNull(resultado);
        verify(rolRepository).save(rol);
    }

    @Test
    void save_deberiaLanzarExcepcionSiNombreDuplicado() {
        Rol rolExistente = new Rol(1L, RolNombre.GESTOR);
        Rol nuevo = new Rol(null, RolNombre.GESTOR);

        when(rolRepository.findByNombre(RolNombre.GESTOR)).thenReturn(Optional.of(rolExistente));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> rolService.save(nuevo));
        assertTrue(ex.getMessage().contains("Ya existe un rol"));
    }

    @Test
    void save_deberiaLanzarExcepcionSiViolacionIntegridad() {
        Rol rol = new Rol(null, RolNombre.GESTOR);

        when(rolRepository.findByNombre(RolNombre.GESTOR)).thenReturn(Optional.empty());
        when(rolRepository.save(rol)).thenThrow(new DataIntegrityViolationException("clave duplicada"));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> rolService.save(rol));
        assertTrue(ex.getMessage().contains("Violación de integridad"));
    }

    @Test
    void delete_deberiaEliminarSiExiste() {
        when(rolRepository.existsById(1L)).thenReturn(true);

        rolService.delete(1L);

        verify(rolRepository).deleteById(1L);
    }

    @Test
    void delete_deberiaLanzarExcepcionSiNoExiste() {
        when(rolRepository.existsById(99L)).thenReturn(false);

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> rolService.delete(99L));
        assertEquals("No se puede eliminar. Rol no encontrado con ID: 99", ex.getMessage());
    }

    @Test
    void delete_deberiaLanzarExcepcionSiRelacionConDatos() {
        when(rolRepository.existsById(1L)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("relación")).when(rolRepository).deleteById(1L);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> rolService.delete(1L));
        assertTrue(ex.getMessage().contains("No se puede eliminar el rol"));
    }

    @Test
    void delete_deberiaLanzarExcepcionInesperada() {
        when(rolRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Falla inesperada")).when(rolRepository).deleteById(1L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> rolService.delete(1L));
        assertTrue(ex.getMessage().contains("Error inesperado al eliminar el rol"));
    }
}
