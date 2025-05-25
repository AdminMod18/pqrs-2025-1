package com.supermarket.pqrs.service;

import com.supermarket.pqrs.model.Cliente;
import com.supermarket.pqrs.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<Cliente> clientes = List.of(new Cliente());
        when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> result = clienteService.findAll();

        assertEquals(1, result.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> result = clienteService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByNumeroIdentificacion() {
        Cliente cliente = new Cliente();
        cliente.setNumeroIdentificacion("12345");
        when(clienteRepository.findByNumeroIdentificacion("12345"))
                .thenReturn(Optional.of(cliente));

        Optional<Cliente> result = clienteService.findByNumeroIdentificacion("12345");

        assertTrue(result.isPresent());
        assertEquals("12345", result.get().getNumeroIdentificacion());
        verify(clienteRepository, times(1)).findByNumeroIdentificacion("12345");
    }

    @Test
    void testSave() {
        Cliente cliente = new Cliente();
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.save(cliente);

        assertNotNull(result);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testDelete() {
        Long id = 1L;
        doNothing().when(clienteRepository).deleteById(id);

        clienteService.delete(id);

        verify(clienteRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindById_noExiste() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Cliente> result = clienteService.findById(99L);

        assertFalse(result.isPresent());
        verify(clienteRepository).findById(99L);
    }

    @Test
    void testFindByNumeroIdentificacion_noExiste() {
        when(clienteRepository.findByNumeroIdentificacion("99999")).thenReturn(Optional.empty());

        Optional<Cliente> result = clienteService.findByNumeroIdentificacion("99999");

        assertFalse(result.isPresent());
        verify(clienteRepository).findByNumeroIdentificacion("99999");
    }
}
