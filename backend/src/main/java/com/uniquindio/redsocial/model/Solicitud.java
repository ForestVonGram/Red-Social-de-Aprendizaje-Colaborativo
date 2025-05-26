package com.uniquindio.redsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "solicitud")
public class Solicitud implements Comparable<Solicitud> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String estudiante;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private int prioridad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderador_id")
    @JsonIgnoreProperties({"solicitudesAsignadas", "solicitudesAyudaAsignadas"})
    private Moderador moderadorAsignado;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Column(nullable = false)
    private boolean resuelta = false;

    @Override
    public int compareTo(Solicitud otra) {
        return Integer.compare(otra.prioridad, this.prioridad);
    }

    public Solicitud(String estudiante, String descripcion, int prioridad) {
        this.estudiante = estudiante;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.fechaCreacion = LocalDateTime.now();
    }
}