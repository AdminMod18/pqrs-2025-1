package com.supermarket.pqrs.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Anexo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombreArchivo;

    @NotBlank
    private String rutaArchivo;

    @NotBlank
    private String tipoArchivo;

    @OneToOne
    @JoinColumn(name = "radicado_id")
    @JsonBackReference
    private Radicado radicado;

}
