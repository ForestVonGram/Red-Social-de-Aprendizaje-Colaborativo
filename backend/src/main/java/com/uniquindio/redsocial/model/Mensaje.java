package com.uniquindio.redsocial.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mensaje {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "conversacion_id")
    private Conversacion conversacion;

    @ManyToOne
    @JoinColumn(name = "remitente_id")
    private Usuario remitente;

    private String contenido;

    private LocalDateTime fecha;

}
