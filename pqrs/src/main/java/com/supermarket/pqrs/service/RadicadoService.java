package com.supermarket.pqrs.service;

import com.supermarket.pqrs.model.*;
import com.supermarket.pqrs.repository.*;
import jakarta.transaction.Transactional;
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

    /**
     * Registra un nuevo radicado para un cliente existente.
     */
    @Transactional
    public Radicado radicarPQRS(Long clienteId, TipoRadicado tipo, String comentarios, Anexo anexo) {
        if (clienteId == null) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio.");
        }

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clienteId));

        Radicado radicado = Radicado.builder()
                .cliente(cliente)
                .numeroRadicado(generarNumeroRadicadoUnico())
                .fechaRadicado(LocalDateTime.now())
                .tipo(tipo != null ? tipo : TipoRadicado.QUEJA)
                .comentarios(comentarios)
                .estado(EstadoRadicado.NUEVO)
                .build();

        if (esAnexoValido(anexo)) {
            anexo.setRadicado(radicado);
            radicado.setAnexo(anexoRepository.save(anexo));
        }

        return radicadoRepository.save(radicado);
    }

    /**
     * Lista todos los radicados.
     */
    public List<Radicado> listarTodos() {
        return radicadoRepository.findAll();
    }

    /**
     * Busca un radicado por su ID.
     */
    public Radicado obtenerPorId(Long id) {
        return radicadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Radicado no encontrado con ID: " + id));
    }

    /**
     * Actualiza un radicado existente sin duplicar registros.
     */
    @Transactional
    public Radicado actualizarRadicado(Long id, TipoRadicado tipo, String comentarios, Anexo anexo) {
        Radicado existente = obtenerPorId(id);

        if (tipo != null) {
            existente.setTipo(tipo);
        }

        if (comentarios != null) {
            existente.setComentarios(comentarios);
        }

        if (esAnexoValido(anexo)) {
            if (existente.getAnexo() != null) {
                Anexo anexoExistente = existente.getAnexo();
                anexoExistente.setTipoArchivo(anexo.getTipoArchivo());
                anexoExistente.setRutaArchivo(anexo.getRutaArchivo());
                anexoExistente.setNombreArchivo(anexo.getNombreArchivo());
                anexoRepository.save(anexoExistente);
            } else {
                anexo.setRadicado(existente);
                existente.setAnexo(anexoRepository.save(anexo));
            }
        }

        return radicadoRepository.save(existente);
    }

    /**
     * Elimina un radicado por su ID.
     */
    public void eliminarRadicado(Long id) {
        if (!radicadoRepository.existsById(id)) {
            throw new IllegalArgumentException("Radicado no encontrado con ID: " + id);
        }
        radicadoRepository.deleteById(id);
    }

    /**
     * Lista los radicados de un cliente por número de identificación.
     */
    public List<Radicado> obtenerRadicadosCliente(String numeroIdentificacion) {
        if (numeroIdentificacion == null || numeroIdentificacion.isBlank()) {
            throw new IllegalArgumentException("Número de identificación no puede estar vacío.");
        }
        return radicadoRepository.findByCliente_NumeroIdentificacion(numeroIdentificacion);
    }

    /**
     * Genera un número de radicado aleatorio de 8 caracteres.
     */
    private String generarNumeroRadicadoUnico() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Verifica si el anexo tiene contenido válido.
     */
    private boolean esAnexoValido(Anexo anexo) {
        return anexo != null &&
                anexo.getTipoArchivo() != null && !anexo.getTipoArchivo().isBlank() &&
                anexo.getRutaArchivo() != null && !anexo.getRutaArchivo().isBlank() &&
                anexo.getNombreArchivo() != null && !anexo.getNombreArchivo().isBlank();
    }
}
