package com.supermarket.pqrs.controller;

import com.supermarket.pqrs.exception.ResourceNotFoundException;
import com.supermarket.pqrs.model.Anexo;
import com.supermarket.pqrs.service.AnexoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/anexos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnexoController {

    private final AnexoService anexoService;

    @GetMapping
    public ResponseEntity<List<Anexo>> getAll() {
        return ResponseEntity.ok(anexoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anexo> getById(@PathVariable Long id) {
        Anexo anexo = anexoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anexo no encontrado con ID: " + id));
        return ResponseEntity.ok(anexo);
    }

    @PostMapping
    public ResponseEntity<Anexo> create(@RequestBody Anexo anexo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(anexoService.save(anexo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Anexo> update(@PathVariable Long id, @RequestBody Anexo anexo) {
        anexoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar. Anexo no encontrado con ID: " + id));
        anexo.setId(id);
        return ResponseEntity.ok(anexoService.save(anexo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        anexoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar. Anexo no encontrado con ID: " + id));
        anexoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<Anexo> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        Anexo anexo = anexoService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(anexo);
    }
}
