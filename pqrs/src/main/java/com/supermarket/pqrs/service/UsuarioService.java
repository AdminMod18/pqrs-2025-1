package com.supermarket.pqrs.service;

import com.supermarket.pqrs.model.Rol;
import com.supermarket.pqrs.model.Usuario;
import com.supermarket.pqrs.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario save(Usuario usuario) {
        // Encriptamos si el password no está ya encriptado
        if (!usuario.getPassword().startsWith("$2")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Método para crear usuario con encriptación y asignación de roles
    public Usuario crearNuevoUsuario(String username, String rawPassword, Set<Rol> roles) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(rawPassword));
        usuario.setRoles(roles);
        return usuarioRepository.save(usuario);
    }
}
