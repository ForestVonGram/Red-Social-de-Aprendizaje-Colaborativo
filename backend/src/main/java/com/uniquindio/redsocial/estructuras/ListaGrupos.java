package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ListaGrupos {
    private class NodoGrupo {
        Usuario usuario;
        NodoGrupo siguiente;

        public NodoGrupo(Usuario usuario) {
            this.usuario = usuario;
        }
    }

    private NodoGrupo cabeza;

    public void agregarUsuario(Usuario usuario) {
        NodoGrupo nuevo = new NodoGrupo(usuario);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
    }

    public void eliminarUsuarioPorCorreo(String correo) {
        if (cabeza == null) return;

        if (cabeza.usuario.getCorreo().equalsIgnoreCase(correo)) {
            cabeza = cabeza.siguiente;
            return;
        }

        NodoGrupo actual = cabeza;
        while (actual.siguiente != null &&
                !actual.siguiente.usuario.getCorreo().equalsIgnoreCase(correo)) {
            actual = actual.siguiente;
        }

        if (actual.siguiente != null) {
            actual.siguiente = actual.siguiente.siguiente;
        }
    }

    public List<Usuario> toList() {
        List<Usuario> usuarios = new ArrayList<>();
        NodoGrupo actual = cabeza;
        while (actual != null) {
            usuarios.add(actual.usuario);
            actual = actual.siguiente;
        }
        return usuarios;
    }
}
