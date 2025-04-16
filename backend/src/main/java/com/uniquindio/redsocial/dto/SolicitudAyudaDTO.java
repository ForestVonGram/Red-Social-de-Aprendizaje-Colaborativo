package com.uniquindio.redsocial.dto;

import com.uniquindio.redsocial.model.SolicitudAyuda;
import lombok.Data;

@Data
public class SolicitudAyudaDTO {
    private String correoEstudiante;
    private String descripcion;
    private SolicitudAyuda.Prioridad prioridad;
}
