package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Usuario;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GrafoUsuarios {
    private final Map<String, Usuario> usuarios = new HashMap<>();
    private final Map<String, Set<String>> conexiones = new HashMap<>();

    public void agregarUsuario(Usuario usuario) {
        usuarios.putIfAbsent(usuario.getCorreo(), usuario);
        conexiones.putIfAbsent(usuario.getCorreo(), new HashSet<>());
    }

    public void conectarUsuarios(String correo1, String correo2) {
        if (usuarios.containsKey(correo1) && usuarios.containsKey(correo2)) {
            conexiones.get(correo1).add(correo2);
            conexiones.get(correo2).add(correo1);
        }
    }

    public Set<Usuario> obtenerAmigos(String correo) {
        Set<Usuario> amigos = new HashSet<>();
        Set<String> conexionesUsuario = conexiones.getOrDefault(correo, new HashSet<>());
        for (String correoAmigo : conexionesUsuario) {
            amigos.add(usuarios.get(correoAmigo));
        }
        return amigos;
    }

    public Set<Usuario> recomendarAmigos(String correo) {
        Set<Usuario> recomendados = new HashSet<>();
        Set<String> amigosDirectos = conexiones.getOrDefault(correo, new HashSet<>());

        for (String amigo : amigosDirectos) {
            Set<String> amigosDeAmigo = conexiones.getOrDefault(amigo, new HashSet<>());
            for (String posible : amigosDeAmigo) {
                if (!posible.equals(correo) && !amigosDirectos.contains(posible)) {
                    recomendados.add(usuarios.get(posible));
                }
            }
        }

        return recomendados;
    }
}
