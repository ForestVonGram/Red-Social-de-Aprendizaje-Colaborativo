package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Usuario;

import java.util.*;

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

    public void eliminarConexion(String correo1, String correo2) {
        if (usuarios.containsKey(correo1) && usuarios.containsKey(correo2)) {
            conexiones.getOrDefault(correo1, new HashSet<>()).remove(correo2);
            conexiones.getOrDefault(correo2, new HashSet<>()).remove(correo1);
        }
    }

    public boolean estanConectados(String correo1, String correo2) {
        return conexiones.getOrDefault(correo1, new HashSet<>()).contains(correo2);
    }

    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public Map<String, Set<String>> getConexiones() {
        return conexiones;
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

    public List<Usuario> buscarRutaMasCorta(String correoOrigen, String correoDestino) {
        if (!usuarios.containsKey(correoOrigen) || !usuarios.containsKey(correoDestino)) {
            return Collections.emptyList();
        }

        Queue<String> cola = new LinkedList<>();
        Map<String, String> predecesores = new HashMap<>();
        Set<String> visitados = new HashSet<>();

        cola.add(correoOrigen);
        visitados.add(correoOrigen);

        while (!cola.isEmpty()) {
            String actual = cola.poll();

            if (actual.equals(correoDestino)) {
                break;
            }

            for (String vecino : conexiones.getOrDefault(actual, new HashSet<>())) {
                if (!visitados.contains(vecino)) {
                    cola.add(vecino);
                    visitados.add(vecino);
                    predecesores.put(actual, vecino);
                }
            }
        }

        List<Usuario> ruta = new LinkedList<>();
        String paso = correoDestino;

        while (paso != null && predecesores.containsKey(paso)) {
            ruta.add(0, usuarios.get(paso));
            paso = predecesores.get(paso);
        }

        if (paso != null && paso.equals(correoOrigen)) {

            ruta.add(0, usuarios.get(correoOrigen));
            return ruta;
        }

        return Collections.emptyList();
    }
}
