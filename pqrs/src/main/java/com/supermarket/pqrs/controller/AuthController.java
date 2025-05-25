package com.supermarket.pqrs.controller;

import com.supermarket.pqrs.exception.ResourceNotFoundException;
import com.supermarket.pqrs.model.AuthRequest;
import com.supermarket.pqrs.model.AuthResponse;
import com.supermarket.pqrs.model.Usuario;
import com.supermarket.pqrs.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            // Error como usuario no encontrado o contraseña incorrecta
            return ResponseEntity.badRequest().body(new AuthResponse(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new AuthResponse(null, "Error interno en el login"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(authService.register(usuario));
        } catch (IllegalArgumentException e) {
            // Por ejemplo: usuario duplicado o datos inválidos
            return ResponseEntity.badRequest().body(new AuthResponse(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new AuthResponse(null, "Error al registrar el usuario"));
        }
    }
}
