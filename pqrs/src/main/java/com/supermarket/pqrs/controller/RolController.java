package com.supermarket.pqrs.controller;

import com.supermarket.pqrs.exception.ResourceNotFoundException;
import com.supermarket.pqrs.model.Rol;
import com.supermarket.pqrs.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RolController {

    private final RolService rolService;

    // Listar todos los roles
    @GetMapping
    public ResponseEntity<List<Rol>> getAll() {
        return ResponseEntity.ok(rolService.findAll());
    }

    // Obtener rol por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Rol rol = rolService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + id));
            return ResponseEntity.ok(rol);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener el rol");
        }
    }

    // Crear nuevo rol
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Rol rol) {
        try {
            Rol creado = rolService.save(rol);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error al crear rol: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno al crear rol");
        }
    }

    // Actualizar rol
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Rol rol) {
        try {
            rolService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar. Rol no encontrado con ID: " + id));

            rol.setId(id);
            Rol actualizado = rolService.save(rol);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error al actualizar rol: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno al actualizar rol");
        }
    }

    // Eliminar rol
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            rolService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar el rol porque está relacionado con otros datos.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado al eliminar rol");
        }
    }
}
