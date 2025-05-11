package com.supermarket.pqrs.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Radicado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroRadicado;

    private LocalDateTime fechaRadicado;

    @Enumerated(EnumType.STRING)
    private TipoRadicado tipo;

    @Lob
    private String comentarios;

    private String estado; // Nuevo, En proceso, Resuelto, Rechazado

    private String justificacion;

    @ManyToOne
    private Cliente cliente;

    @OneToOne(cascade = CascadeType.ALL)
    private Anexo anexo;
}
