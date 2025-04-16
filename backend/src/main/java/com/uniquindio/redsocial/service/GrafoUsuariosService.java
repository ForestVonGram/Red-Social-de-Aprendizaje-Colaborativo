package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.GrafoUsuarios;
import com.uniquindio.redsocial.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GrafoUsuariosService {
    private final GrafoUsuarios grafoUsuarios;

    public GrafoUsuariosService(GrafoUsuarios grafoUsuarios) {
        this.grafoUsuarios = grafoUsuarios;
    }

    public List<Usuario> buscarRutaMasCorta(String correoOrigen, String correoDestino) {
        return grafoUsuarios.buscarRutaMasCorta(correoOrigen, correoDestino);
    }
}