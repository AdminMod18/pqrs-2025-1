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
@RequestMapping("api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(
                    new AuthResponse("Error: " + e.getMessage(), null, null)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new AuthResponse("Error interno en el login", null, null)
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(authService.register(usuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new AuthResponse("Error: " + e.getMessage(), null, null)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new AuthResponse("Error al registrar el usuario", null, null)
            );
        }
    }
}
