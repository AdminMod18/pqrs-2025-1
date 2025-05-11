package com.supermarket.pqrs.controller;

import com.supermarket.pqrs.model.Anexo;
import com.supermarket.pqrs.service.AnexoService;
import lombok.RequiredArgsConstructor;
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
        return anexoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Anexo> create(@RequestBody Anexo anexo) {
        return ResponseEntity.ok(anexoService.save(anexo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Anexo> update(@PathVariable Long id, @RequestBody Anexo anexo) {
        anexo.setId(id);
        return ResponseEntity.ok(anexoService.save(anexo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        anexoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<Anexo> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Anexo anexo = anexoService.uploadFile(file);
            return ResponseEntity.ok(anexo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



}
