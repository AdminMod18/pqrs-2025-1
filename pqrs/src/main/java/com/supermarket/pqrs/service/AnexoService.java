package com.supermarket.pqrs.service;

import com.supermarket.pqrs.model.Anexo;
import com.supermarket.pqrs.repository.AnexoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnexoService {

    private final AnexoRepository anexoRepository;

    // Ruta donde se guardan los archivos
    private static final String UPLOAD_DIR = "src/main/resources/uploads/";

    // Método para obtener todos los anexos
    public List<Anexo> findAll() {
        return anexoRepository.findAll();
    }

    // Método para obtener un anexo por su ID
    public Optional<Anexo> findById(Long id) {
        return anexoRepository.findById(id);
    }

    // Método para guardar un anexo
    public Anexo save(Anexo anexo) {
        return anexoRepository.save(anexo);
    }

    // Método para eliminar un anexo
    public void delete(Long id) {
        anexoRepository.deleteById(id);
    }

    // Método para manejar la carga del archivo
    public Anexo uploadFile(MultipartFile file) throws IOException {
        // Generamos un nombre único para el archivo
        String uniqueFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        // Creamos la ruta completa donde se guardará el archivo
        Path path = Paths.get(UPLOAD_DIR + uniqueFileName);

        // Guardamos el archivo en el sistema de archivos
        Files.write(path, file.getBytes());

        // Creamos el objeto Anexo y asignamos los valores
        Anexo anexo = new Anexo();
        anexo.setNombreArchivo(file.getOriginalFilename());  // Guardamos el nombre original
        anexo.setRutaArchivo(path.toString());  // Guardamos la ruta completa del archivo
        anexo.setTipoArchivo(file.getContentType());  // Guardamos el tipo MIME del archivo

        // Guardamos el anexo en la base de datos
        return anexoRepository.save(anexo);
    }
}
