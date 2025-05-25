package com.supermarket.pqrs.repository;

import com.supermarket.pqrs.model.Rol;
import com.supermarket.pqrs.model.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(RolNombre nombre);
}
