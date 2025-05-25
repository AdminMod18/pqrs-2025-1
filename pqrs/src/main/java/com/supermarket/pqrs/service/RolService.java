package com.supermarket.pqrs.service;

import com.supermarket.pqrs.exception.ResourceNotFoundException;
import com.supermarket.pqrs.model.Rol;
import com.supermarket.pqrs.model.RolNombre;
import com.supermarket.pqrs.repository.RolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    public Optional<Rol> findById(Long id) {
        return rolRepository.findById(id);
    }

    public Rol save(Rol rol) {
        try {
            // Validación: no permitir duplicados
            Optional<Rol> existente = rolRepository.findByNombre(rol.getNombre());
            if (existente.isPresent() && !existente.get().getId().equals(rol.getId())) {
                throw new IllegalArgumentException("Ya existe un rol con el nombre: " + rol.getNombre());
            }

            return rolRepository.save(rol);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Violación de integridad al guardar el rol: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al guardar el rol", e);
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. Rol no encontrado con ID: " + id);
        }

        try {
            rolRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("No se puede eliminar el rol porque está relacionado con otros datos.");
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al eliminar el rol", e);
        }
    }
}
