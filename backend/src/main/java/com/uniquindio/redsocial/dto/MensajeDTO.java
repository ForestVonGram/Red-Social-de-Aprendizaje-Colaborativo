package com.uniquindio.redsocial.dto;

import lombok.Data;

@Data
public class MensajeDTO {
    private Long remitenteId;
    private Long conversacionId;
    private String contenido;
}
