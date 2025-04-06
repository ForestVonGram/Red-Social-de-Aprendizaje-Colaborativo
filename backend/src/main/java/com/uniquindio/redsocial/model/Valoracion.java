package com.uniquindio.redsocial.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Valoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private int estrellas;
    private String comentario;

    @ManyToOne
    private Contenido contenido;
}
