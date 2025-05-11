package com.supermarket.pqrs.controller;

import com.supermarket.pqrs.model.*;
import com.supermarket.pqrs.service.RadicadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/radicados")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RadicadoController {

    private final RadicadoService radicadoService;

    @PostMapping("/registrar")
    public ResponseEntity<Radicado> registrarPQRS(@RequestBody PQRSRequest request) {
        Radicado radicado = radicadoService.radicarPQRS(
                request.getCliente(),
                request.getTipo(),
                request.getComentarios(),
                request.getAnexo()
        );
        return ResponseEntity.ok(radicado);
    }

    @GetMapping("/cliente/{numeroIdentificacion}")
    public ResponseEntity<List<Radicado>> listarPorCliente(@PathVariable String numeroIdentificacion) {
        return ResponseEntity.ok(radicadoService.obtenerRadicadosCliente(numeroIdentificacion));
    }

    // DTO interno para simplificar la petición
    public static class PQRSRequest {
        private Cliente cliente;
        private TipoRadicado tipo;
        private String comentarios;
        private Anexo anexo;

        // Getters y Setters
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
