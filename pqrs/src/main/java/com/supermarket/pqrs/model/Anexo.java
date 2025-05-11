package com.supermarket.pqrs.model;

import jakarta.persistence.*;
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

    // Nombre del archivo subido
    private String nombreArchivo;

    // Ruta donde se guarda el archivo en el sistema de archivos
    private String rutaArchivo;

    // Tipo de archivo (por ejemplo, PDF, JPG, etc.)
    private String tipoArchivo;
}
