package com.uniquindio.redsocial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudAyuda implements Comparable<SolicitudAyuda> {
    private String id;
    private String descripcion;
    private String correoEstudiante;
    private Prioridad prioridad;

    public enum Prioridad {
        ALTA, MEDIA, BAJA
    }

    @Override
    public int compareTo(SolicitudAyuda otra) {
        return otra.prioridad.ordinal() - this.prioridad.ordinal();
    }
}
