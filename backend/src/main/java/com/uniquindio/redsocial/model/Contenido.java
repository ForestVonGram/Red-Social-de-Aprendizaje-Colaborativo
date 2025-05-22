package com.uniquindio.redsocial.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Contenido {

    @Id
    private Long id;

    private String titulo;
    private String descripcion;
    private String tipo; // "VIDEO", "IMAGEN", "DOCUMENTO"
    private String url;

    @ManyToOne
    private Usuario autor;

    private Date fechaPublicacion;

    @OneToMany(mappedBy = "contenido", cascade = CascadeType.ALL)
    private List<Valoracion> valoraciones;
}
