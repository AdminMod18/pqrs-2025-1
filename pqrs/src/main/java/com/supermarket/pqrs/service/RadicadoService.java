package com.supermarket.pqrs.service;

import com.supermarket.pqrs.model.*;
import com.supermarket.pqrs.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RadicadoService {

    private final ClienteRepository clienteRepository;
    private final RadicadoRepository radicadoRepository;
    private final AnexoRepository anexoRepository;

    public Radicado radicarPQRS(Cliente clienteInput, TipoRadicado tipo, String comentarios, Anexo anexo) {
        Cliente cliente = clienteRepository
                .findByNumeroIdentificacion(clienteInput.getNumeroIdentificacion())
                .orElseGet(() -> clienteRepository.save(clienteInput));

        Anexo savedAnexo = anexo != null ? anexoRepository.save(anexo) : null;

        Radicado radicado = Radicado.builder()
                .cliente(cliente)
                .numeroRadicado(UUID.randomUUID().toString().substring(0, 8)) // Ej: 8 caracteres aleatorios
                .fechaRadicado(LocalDateTime.now())
                .tipo(tipo)
                .comentarios(comentarios)
                .estado("Nuevo")
                .anexo(savedAnexo)
                .build();

        return radicadoRepository.save(radicado);
    }

    public List<Radicado> obtenerRadicadosCliente(String numeroIdentificacion) {
        return radicadoRepository.findByCliente_NumeroIdentificacion(numeroIdentificacion);
    }
}
