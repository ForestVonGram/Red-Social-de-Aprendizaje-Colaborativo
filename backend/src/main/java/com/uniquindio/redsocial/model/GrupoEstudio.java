package com.uniquindio.redsocial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoEstudio {
    private List<Usuario> miembros;
    private String temaCentral;
}
