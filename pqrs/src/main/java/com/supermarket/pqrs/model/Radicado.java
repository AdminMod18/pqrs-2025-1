package com.supermarket.pqrs.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(unique = true, nullable = false)
    private String numeroRadicado;

    @Column(nullable = false)
    private LocalDateTime fechaRadicado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRadicado tipo;

    @Lob
    private String comentarios;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRadicado estado;

    private String justificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToOne(mappedBy = "radicado", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Anexo anexo;
}
