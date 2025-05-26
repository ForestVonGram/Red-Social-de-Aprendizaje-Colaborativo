package com.uniquindio.redsocial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Moderador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String correo;
    private String contrasenia;

    @OneToMany
    private List<Solicitud> solicitudesAsignadas;

    @OneToMany
    private List<SolicitudAyuda> solicitudesAyudaAsignadas;
}
