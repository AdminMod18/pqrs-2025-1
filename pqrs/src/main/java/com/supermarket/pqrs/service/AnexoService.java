package com.supermarket.pqrs.service;

import com.supermarket.pqrs.model.Anexo;
import com.supermarket.pqrs.repository.AnexoRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnexoService {

    private final AnexoRepository anexoRepository;

    // Ruta base donde se guardan los archivos
    private static final String UPLOAD_DIR = "src/main/resources/uploads/";

    @PostConstruct
    public void crearDirectorioSiNoExiste() {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de uploads", e);
        }
    }

    // Obtener todos los anexos
    public List<Anexo> findAll() {
        return anexoRepository.findAll();
    }

    // Buscar por ID
    public Optional<Anexo> findById(Long id) {
        return anexoRepository.findById(id);
    }

    // Guardar anexo en BD (uso interno)
    public Anexo save(Anexo anexo) {
        return anexoRepository.save(anexo);
    }

    // Eliminar anexo y su archivo físico
    public void delete(Long id) {
        Optional<Anexo> optionalAnexo = anexoRepository.findById(id);
        optionalAnexo.ifPresent(anexo -> {
            try {
                Path path = Paths.get(anexo.getRutaArchivo());
                Files.deleteIfExists(path);
            } catch (IOException e) {
                System.err.println("No se pudo eliminar el archivo físico: " + e.getMessage());
            }
            anexoRepository.deleteById(id);
        });
    }

    // Cargar archivo y guardar en BD
    public Anexo uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = obtenerExtension(originalFilename);
        String uniqueFileName = UUID.randomUUID() + (extension != null ? "." + extension : "");

        Path destinationPath = Paths.get(UPLOAD_DIR + uniqueFileName);
        Files.write(destinationPath, file.getBytes(), StandardOpenOption.CREATE);

        Anexo anexo = new Anexo();
        anexo.setNombreArchivo(originalFilename);
        anexo.setRutaArchivo(destinationPath.toString());
        anexo.setTipoArchivo(file.getContentType());

        return anexoRepository.save(anexo);
    }

    // Utilidad: obtener extensión del archivo
    private String obtenerExtension(String filename) {
        if (filename == null) return null;
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot + 1) : null;
    }
}
