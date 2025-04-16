package com.uniquindio.redsocial.dto;

import lombok.Data;

import java.util.List;

@Data
public class UsuarioDTO {
    private String nombre;
    private String correo;
    private String contrasenia;
    private List<String> intereses;
}

