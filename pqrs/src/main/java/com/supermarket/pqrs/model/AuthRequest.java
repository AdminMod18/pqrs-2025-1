package com.supermarket.pqrs.model;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
