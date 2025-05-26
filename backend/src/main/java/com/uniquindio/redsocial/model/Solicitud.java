package com.uniquindio.redsocial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solicitud implements Comparable<Solicitud>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String estudiante;
    private String descripcion;
    private int prioridad;

    @Override
    public int compareTo(Solicitud otra) {
        return Integer.compare(this.prioridad, otra.prioridad);
    }
}
