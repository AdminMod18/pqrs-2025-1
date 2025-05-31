package com.supermarket.pqrs.service;

import com.supermarket.pqrs.model.Anexo;
import com.supermarket.pqrs.repository.AnexoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnexoServiceTest {

    @Mock
    private AnexoRepository anexoRepository;

    @InjectMocks
    private AnexoService anexoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        List<Anexo> anexos = List.of(new Anexo());
        when(anexoRepository.findAll()).thenReturn(anexos);

        List<Anexo> result = anexoService.findAll();

        assertEquals(1, result.size());
        verify(anexoRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        Anexo anexo = new Anexo();
        anexo.setId(1L);
        when(anexoRepository.findById(1L)).thenReturn(Optional.of(anexo));

        Optional<Anexo> result = anexoService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void save() {
        Anexo anexo = new Anexo();
        when(anexoRepository.save(anexo)).thenReturn(anexo);

        Anexo result = anexoService.save(anexo);

        assertNotNull(result);
        verify(anexoRepository, times(1)).save(anexo);
    }

    @Test
    void delete() {
        Anexo anexo = new Anexo();
        anexo.setId(1L);
        anexo.setRutaArchivo("src/test/resources/testfile.txt"); // Simula ruta real

        when(anexoRepository.findById(1L)).thenReturn(Optional.of(anexo));
        doNothing().when(anexoRepository).deleteById(1L);

        // Crear archivo simulado para que la eliminación no falle
        try {
            Files.createDirectories(Path.of("src/test/resources"));
            Files.createFile(Path.of(anexo.getRutaArchivo()));
        } catch (IOException ignored) {}

        anexoService.delete(1L);

        verify(anexoRepository, times(1)).findById(1L);
        verify(anexoRepository, times(1)).deleteById(1L);

        // Limpieza
        try {
            Files.deleteIfExists(Path.of(anexo.getRutaArchivo()));
        } catch (IOException ignored) {}
    }

    @Test
    void uploadFile() throws IOException {
        String fileName = "test.txt";
        MockMultipartFile file = new MockMultipartFile(
                "file", fileName, "text/plain", "contenido de prueba".getBytes());

        when(anexoRepository.save(any(Anexo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Anexo result = anexoService.uploadFile(file);

        assertNotNull(result);
        assertEquals(fileName, result.getNombreArchivo());
        assertEquals("text/plain", result.getTipoArchivo());

        // Limpieza del archivo si existe
        Path path = Path.of(result.getRutaArchivo());
        assertTrue(Files.exists(path));
        Files.deleteIfExists(path);
    }

    @Test
    void crearDirectorioSiNoExiste_creaDirectorio() {
        assertDoesNotThrow(() -> anexoService.crearDirectorioSiNoExiste());
        assertTrue(Files.exists(Path.of("src/main/resources/uploads/")));
    }

    @Test
    void uploadFile_deberiaLanzarExcepcionSiArchivoVacio() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.txt", "text/plain", new byte[0]);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> anexoService.uploadFile(emptyFile));
        assertEquals("El archivo está vacío", ex.getMessage());
    }
}
//