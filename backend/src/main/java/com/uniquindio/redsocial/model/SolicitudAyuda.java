package com.uniquindio.redsocial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudAyuda implements Comparable<SolicitudAyuda> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private String correoEstudiante;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    public enum Prioridad {
        ALTA, MEDIA, BAJA
    }

    @Override
    public int compareTo(SolicitudAyuda otra) {
        return otra.prioridad.ordinal() - this.prioridad.ordinal();
    }
}
