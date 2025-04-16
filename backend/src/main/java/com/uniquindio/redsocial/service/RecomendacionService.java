package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.GrafoUsuarios;
import com.uniquindio.redsocial.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RecomendacionService {
    private final GrafoUsuarios grafo = new GrafoUsuarios();

    public void registrarUsuario(Usuario usuario) {
        grafo.agregarUsuario(usuario);
    }

    public void conectar(String correo1, String correo2) {
        grafo.conectarUsuarios(correo1, correo2);
    }

    public Set<Usuario> obtenerRecomendaciones(String correo) {
        return grafo.recomendarAmigos(correo);
    }
}
