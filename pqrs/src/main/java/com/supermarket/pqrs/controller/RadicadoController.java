package com.supermarket.pqrs.controller;

import com.supermarket.pqrs.exception.ResourceNotFoundException;
import com.supermarket.pqrs.model.*;
import com.supermarket.pqrs.service.RadicadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/radicados")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RadicadoController {

    private final RadicadoService radicadoService;

    // Crear nuevo radicado
    @PostMapping("/registrar")
    public ResponseEntity<Radicado> registrarPQRS(@RequestBody PQRSRequest request) {
        if (request.getCliente() == null || request.getCliente().getId() == null) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio para registrar el radicado.");
        }

        Radicado radicado = radicadoService.radicarPQRS(
                request.getCliente().getId(),
                request.getTipo(),
                request.getComentarios(),
                request.getAnexo()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(radicado);
    }

    // Obtener radicados por número de identificación
    @GetMapping("/cliente/{numeroIdentificacion}")
    public ResponseEntity<List<Radicado>> listarPorCliente(@PathVariable String numeroIdentificacion) {
        List<Radicado> radicados = radicadoService.obtenerRadicadosCliente(numeroIdentificacion);
        return ResponseEntity.ok(radicados);
    }

    // Obtener todos los radicados
    @GetMapping
    public ResponseEntity<List<Radicado>> listarTodos() {
        return ResponseEntity.ok(radicadoService.listarTodos());
    }

    // Obtener radicado por ID
    @GetMapping("/{id}")
    public ResponseEntity<Radicado> obtenerPorId(@PathVariable Long id) {
        Radicado radicado = radicadoService.obtenerPorId(id);
        return ResponseEntity.ok(radicado);
    }

    // Actualizar radicado existente
    @PutMapping("/{id}")
    public ResponseEntity<Radicado> actualizar(@PathVariable Long id, @RequestBody PQRSRequest request) {
        Radicado actualizado = radicadoService.actualizarRadicado(
                id,
                request.getTipo(),
                request.getComentarios(),
                request.getAnexo()
        );
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar radicado por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        radicadoService.eliminarRadicado(id);
        return ResponseEntity.noContent().build();
    }

    // DTO interno para registrar y actualizar
    public static class PQRSRequest {
        private Cliente cliente;
        private TipoRadicado tipo;
        private String comentarios;
        private Anexo anexo;

        public Cliente getCliente() { return cliente; }
        public void setCliente(Cliente cliente) { this.cliente = cliente; }

        public TipoRadicado getTipo() { return tipo; }
        public void setTipo(TipoRadicado tipo) { this.tipo = tipo; }

        public String getComentarios() { return comentarios; }
        public void setComentarios(String comentarios) { this.comentarios = comentarios; }

        public Anexo getAnexo() { return anexo; }
        public void setAnexo(Anexo anexo) { this.anexo = anexo; }
    }
}
