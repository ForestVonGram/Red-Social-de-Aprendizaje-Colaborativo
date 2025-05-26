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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "solicitud_ayuda")
public class SolicitudAyuda implements Comparable<SolicitudAyuda> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Column(name = "correo_estudiante", nullable = false)
    private String correoEstudiante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridad prioridad;

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

    public enum Prioridad {
        ALTA, MEDIA, BAJA
    }

    @Override
    public int compareTo(SolicitudAyuda otra) {
        return otra.prioridad.ordinal() - this.prioridad.ordinal();
    }

    // Constructor personalizado
    public SolicitudAyuda(String descripcion, String correoEstudiante, Prioridad prioridad) {
        this.descripcion = descripcion;
        this.correoEstudiante = correoEstudiante;
        this.prioridad = prioridad;
        this.fechaCreacion = LocalDateTime.now();
    }
}