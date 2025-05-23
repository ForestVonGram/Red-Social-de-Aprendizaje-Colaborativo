package com.uniquindio.redsocial.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String correo;
    private String contrasenia;

    @ElementCollection
    private List<String> intereses;

    @ManyToMany(mappedBy = "participantes")
    private List<Conversacion> conversaciones;
}