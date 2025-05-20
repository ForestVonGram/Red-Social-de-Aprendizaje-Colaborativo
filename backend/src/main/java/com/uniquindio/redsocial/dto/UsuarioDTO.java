package com.uniquindio.redsocial.dto;

import com.uniquindio.redsocial.model.Conversacion;
import lombok.Data;

import java.util.List;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String contrasenia;
    private List<String> intereses;
    private List<Conversacion> conversaciones;
}
