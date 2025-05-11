package com.supermarket.pqrs.repository;

import com.supermarket.pqrs.model.Radicado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RadicadoRepository extends JpaRepository<Radicado, Long> {
    List<Radicado> findByCliente_NumeroIdentificacion(String numeroIdentificacion);
}
