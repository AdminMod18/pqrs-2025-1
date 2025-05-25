package com.supermarket.pqrs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor        // Constructor sin argumentos
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
}
