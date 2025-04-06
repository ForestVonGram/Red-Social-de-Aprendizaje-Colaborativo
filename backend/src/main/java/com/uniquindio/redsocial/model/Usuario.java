package com.uniquindio.redsocial.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    private String id;

    private String nombre;
    private String correo;
    private String contrasenia;
}

