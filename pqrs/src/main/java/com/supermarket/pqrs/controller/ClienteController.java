package com.supermarket.pqrs.controller;

import com.supermarket.pqrs.exception.ResourceNotFoundException;
import com.supermarket.pqrs.model.Cliente;
import com.supermarket.pqrs.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    // Listar todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> getAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    // Obtener cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getById(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return ResponseEntity.ok(cliente);
    }

    // Obtener cliente por número de identificación
    @GetMapping("/identificacion/{numeroIdentificacion}")
    public ResponseEntity<Cliente> getByNumeroIdentificacion(@PathVariable String numeroIdentificacion) {
        Cliente cliente = clienteService.findByNumeroIdentificacion(numeroIdentificacion)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con identificación: " + numeroIdentificacion));
        return ResponseEntity.ok(cliente);
    }

    // Crear nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> create(@RequestBody Cliente cliente) {
        Cliente creado = clienteService.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody Cliente cliente) {
        clienteService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar. Cliente no encontrado con ID: " + id));
        cliente.setId(id);
        Cliente actualizado = clienteService.save(cliente);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar. Cliente no encontrado con ID: " + id));
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
