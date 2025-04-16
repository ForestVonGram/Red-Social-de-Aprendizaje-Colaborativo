package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.GrafoUsuarios;
import com.uniquindio.redsocial.model.GrupoEstudio;
import com.uniquindio.redsocial.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GrupoEstudioService {
    private final GrafoUsuarios grafoUsuarios = new GrafoUsuarios();
    private Map<String, Usuario> usuarios = new HashMap<>();

    public void registrarUsuario(Usuario usuario) {
        usuarios.put(usuario.getCorreo(), usuario);
        grafoUsuarios.agregarUsuario(usuario);
    }

    public void conectarUsuarios(String correo1, String correo2) {
        grafoUsuarios.conectarUsuarios(correo1, correo2);
    }

    public List<GrupoEstudio> formarGruposPorIntereses() {
        Set<String> procesados = new HashSet<>();
        List<GrupoEstudio> grupos = new ArrayList<>();

        for (Usuario usuario : usuarios.values()) {
            if (procesados.contains(usuario.getCorreo())) continue;

            Set<Usuario> relacionados = grafoUsuarios.obtenerAmigos(usuario.getCorreo());

            List<Usuario> grupo = new ArrayList<>();
            grupo.add(usuario);

            for (Usuario posible : relacionados) {
                if (!procesados.contains(posible.getCorreo()) &&
                        tienenInteresesEnComun(usuario, posible)) {
                    grupo.add(posible);
                }
            }

            if (grupo.size() > 1) {
                grupos.add(new GrupoEstudio(grupo, obtenerTemaComun(grupo)));
                for (Usuario u : grupo) procesados.add(u.getCorreo());
            }
        }

        return grupos;
    }

    private boolean tienenInteresesEnComun(Usuario u1, Usuario u2) {
        for (String interes : u1.getIntereses()) {
            if (u2.getIntereses().contains(interes)) {
                return true;
            }
        }
        return false;
    }

    private String obtenerTemaComun(List<Usuario> grupo) {
        Map<String, Integer> contador = new HashMap<>();

        for (Usuario u : grupo) {
            for (String interes : u.getIntereses()) {
                contador.put(interes, contador.getOrDefault(interes, 0) + 1);
            }
        }

        return contador.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Interés General");
    }
}
