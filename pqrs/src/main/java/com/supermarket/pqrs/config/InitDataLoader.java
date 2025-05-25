package com.supermarket.pqrs.config;

import com.supermarket.pqrs.model.Rol;
import com.supermarket.pqrs.model.RolNombre;
import com.supermarket.pqrs.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDataLoader implements CommandLineRunner {

    private final RolRepository rolRepository;

    @Override
    public void run(String... args) {
        // Insertar rol GESTOR si no existe
        if (rolRepository.findByNombre(RolNombre.GESTOR).isEmpty()) {
            rolRepository.save(Rol.builder().nombre(RolNombre.GESTOR).build());
            System.out.println("✅ Rol GESTOR creado");
        }

        // Insertar rol CLIENTE si no existe
        if (rolRepository.findByNombre(RolNombre.CLIENTE).isEmpty()) {
            rolRepository.save(Rol.builder().nombre(RolNombre.CLIENTE).build());
            System.out.println("✅ Rol CLIENTE creado");
        }
    }
}
