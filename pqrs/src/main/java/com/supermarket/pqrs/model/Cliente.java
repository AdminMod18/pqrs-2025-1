package com.supermarket.pqrs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String tipoIdentificacion;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String numeroIdentificacion;

    @NotBlank
    @Column(nullable = false)
    private String nombreCompleto;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String correo;

    private String telefono;

    @NotBlank
    @Column(nullable = false)
    private String contrasena;
}
