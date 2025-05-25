package com.supermarket.pqrs.controller;

import com.supermarket.pqrs.exception.ResourceNotFoundException;
import com.supermarket.pqrs.model.Usuario;
import com.supermarket.pqrs.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
            return ResponseEntity.ok(usuario);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener usuario");
        }
    }

    // Crear usuario
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Usuario usuario) {
        try {
            Usuario creado = usuarioService.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Nombre de usuario duplicado. Ya existe un usuario con ese nombre.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error al crear usuario: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno al crear usuario");
        }
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            usuarioService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar. Usuario no encontrado con ID: " + id));

            usuario.setId(id);
            Usuario actualizado = usuarioService.save(usuario);
            return ResponseEntity.ok(actualizado);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Ya existe un usuario con ese nombre de usuario.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar usuario");
        }
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            usuarioService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar. Usuario no encontrado con ID: " + id));

            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar el usuario porque está relacionado con otras entidades.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error inesperado al eliminar usuario: " + e.getMessage());
        }
    }
}
