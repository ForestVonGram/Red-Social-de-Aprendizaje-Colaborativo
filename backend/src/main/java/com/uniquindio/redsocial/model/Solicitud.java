package com.uniquindio.redsocial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solicitud implements Comparable<Solicitud>{
    private String estudiante;
    private String descripcion;
    private int prioridad;

    @Override
    public int compareTo(Solicitud otra) {
        return Integer.compare(this.prioridad, otra.prioridad);
    }
}
