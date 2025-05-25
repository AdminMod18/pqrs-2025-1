package com.supermarket.pqrs.service;

import com.supermarket.pqrs.model.*;
import com.supermarket.pqrs.repository.AnexoRepository;
import com.supermarket.pqrs.repository.ClienteRepository;
import com.supermarket.pqrs.repository.RadicadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RadicadoServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private RadicadoRepository radicadoRepository;

    @Mock
    private AnexoRepository anexoRepository;

    @InjectMocks
    private RadicadoService radicadoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void radicarPQRS_deberiaGuardarRadicadoConAnexo() {
        Cliente cliente = Cliente.builder().id(1L).build();
        Anexo anexo = Anexo.builder()
                .tipoArchivo("image/png")
                .rutaArchivo("ruta/archivo.png")
                .nombreArchivo("archivo.png")
                .build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(anexoRepository.save(any(Anexo.class))).thenAnswer(inv -> inv.getArgument(0));
        when(radicadoRepository.save(any(Radicado.class))).thenAnswer(inv -> inv.getArgument(0));

        Radicado result = radicadoService.radicarPQRS(1L, TipoRadicado.PETICION, "Comentario", anexo);

        assertNotNull(result);
        assertEquals(cliente, result.getCliente());
        assertNotNull(result.getAnexo());
    }

    @Test
    void radicarPQRS_conClienteInexistente_lanzaExcepcion() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> radicadoService.radicarPQRS(99L, TipoRadicado.QUEJA, "comentario", null));
        assertTrue(ex.getMessage().contains("Cliente no encontrado"));
    }

    @Test
    void listarTodos_deberiaRetornarLista() {
        when(radicadoRepository.findAll()).thenReturn(List.of(new Radicado()));
        List<Radicado> lista = radicadoService.listarTodos();
        assertEquals(1, lista.size());
    }

    @Test
    void obtenerPorId_conIdValido() {
        Radicado radicado = new Radicado();
        when(radicadoRepository.findById(1L)).thenReturn(Optional.of(radicado));
        assertEquals(radicado, radicadoService.obtenerPorId(1L));
    }

    @Test
    void obtenerPorId_conIdInvalido_lanzaExcepcion() {
        when(radicadoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> radicadoService.obtenerPorId(1L));
    }

    @Test
    void actualizarRadicado_actualizaCorrectamente() {
        Radicado existente = Radicado.builder().id(1L).comentarios("viejo").build();
        when(radicadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(radicadoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Anexo nuevoAnexo = Anexo.builder()
                .tipoArchivo("image/jpg")
                .rutaArchivo("ruta")
                .nombreArchivo("archivo.jpg")
                .build();

        Radicado actualizado = radicadoService.actualizarRadicado(1L, TipoRadicado.RECLAMO, "nuevo comentario", nuevoAnexo);

        assertEquals("nuevo comentario", actualizado.getComentarios());
        assertEquals(TipoRadicado.RECLAMO, actualizado.getTipo());
    }

    @Test
    void eliminarRadicado_existente() {
        when(radicadoRepository.existsById(1L)).thenReturn(true);
        radicadoService.eliminarRadicado(1L);
        verify(radicadoRepository).deleteById(1L);
    }

    @Test
    void eliminarRadicado_inexistente_lanzaExcepcion() {
        when(radicadoRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> radicadoService.eliminarRadicado(1L));
    }

    @Test
    void obtenerRadicadosCliente_retornaLista() {
        when(radicadoRepository.findByCliente_NumeroIdentificacion("123"))
                .thenReturn(List.of(new Radicado()));
        List<Radicado> lista = radicadoService.obtenerRadicadosCliente("123");
        assertEquals(1, lista.size());
    }

    @Test
    void obtenerRadicadosCliente_conIdentificacionVacia_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> radicadoService.obtenerRadicadosCliente(" "));
    }
    @Test
    void radicarPQRS_conAnexoInvalido_noGuardaAnexo() {
        Cliente cliente = Cliente.builder().id(1L).build();
        Anexo anexoInvalido = new Anexo(); // todos los campos nulos o vacíos

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(radicadoRepository.save(any(Radicado.class))).thenAnswer(inv -> inv.getArgument(0));

        Radicado result = radicadoService.radicarPQRS(1L, TipoRadicado.PETICION, "Comentario", anexoInvalido);

        assertNotNull(result);
        assertNull(result.getAnexo()); // No debe haberse guardado
    }

    @Test
    void actualizarRadicado_sinAnexo_noActualizaAnexo() {
        Radicado existente = Radicado.builder()
                .id(1L)
                .tipo(TipoRadicado.SUGERENCIA)
                .comentarios("antiguo")
                .build();

        when(radicadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(radicadoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Anexo anexoInvalido = new Anexo(); // todos los campos vacíos

        Radicado result = radicadoService.actualizarRadicado(1L, TipoRadicado.RECLAMO, "nuevo", anexoInvalido);

        assertEquals("nuevo", result.getComentarios());
        assertEquals(TipoRadicado.RECLAMO, result.getTipo());
        assertNull(result.getAnexo()); // no se debe modificar anexo
    }
}
