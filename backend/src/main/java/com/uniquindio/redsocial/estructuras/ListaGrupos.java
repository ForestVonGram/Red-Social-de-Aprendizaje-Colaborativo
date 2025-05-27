package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.GrupoEstudio;
import com.uniquindio.redsocial.model.Usuario;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Getter
public class ListaGrupos {

    @Getter
    private static class NodoGrupo {
        private final Usuario usuario;
        private final LocalDateTime fechaIngreso;
        private final GrupoEstudio grupoEstudio;
        private NodoGrupo siguiente;

        public NodoGrupo(Usuario usuario, GrupoEstudio grupoEstudio) {
            this.usuario = usuario;
            this.grupoEstudio = grupoEstudio;
            this.fechaIngreso = LocalDateTime.now();
        }
    }

    private NodoGrupo cabeza;
    private int tamanio;
    private static final int CAPACIDAD_MAXIMA = 50;

    public boolean agregarUsuario(Usuario usuario, GrupoEstudio grupoEstudio) {
        if (usuario == null || grupoEstudio == null) {
            throw new IllegalArgumentException("El usuario y el grupo no pueden ser nulos");
        }

        if (contieneMiembro(usuario.getCorreo())) {
            return false;
        }

        if (tamanio >= CAPACIDAD_MAXIMA) {
            throw new IllegalStateException("El grupo ha alcanzado su capacidad máxima");
        }

        NodoGrupo nuevo = new NodoGrupo(usuario, grupoEstudio);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
        tamanio++;
        return true;
    }

    public boolean eliminarUsuarioPorCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede ser nulo o vacío");
        }

        if (cabeza == null) return false;

        if (cabeza.usuario.getCorreo().equalsIgnoreCase(correo)) {
            cabeza = cabeza.siguiente;
            tamanio--;
            return true;
        }

        NodoGrupo actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.usuario.getCorreo().equalsIgnoreCase(correo)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamanio--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public List<Usuario> obtenerMiembros() {
        return toList();
    }

    public List<Usuario> obtenerMiembrosPorGrupo(GrupoEstudio grupo) {
        List<Usuario> miembros = new ArrayList<>();
        NodoGrupo actual = cabeza;
        while (actual != null) {
            if (actual.grupoEstudio.equals(grupo)) {
                miembros.add(actual.usuario);
            }
            actual = actual.siguiente;
        }
        return miembros;
    }

    public Map<GrupoEstudio, List<Usuario>> obtenerMiembrosPorGrupos() {
        return toList().stream()
                .collect(Collectors.groupingBy(
                        usuario -> encontrarGrupoDeUsuario(usuario.getCorreo()),
                        Collectors.toList()
                ));
    }

    private GrupoEstudio encontrarGrupoDeUsuario(String correo) {
        NodoGrupo actual = cabeza;
        while (actual != null) {
            if (actual.usuario.getCorreo().equals(correo)) {
                return actual.grupoEstudio;
            }
            actual = actual.siguiente;
        }
        return null;
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

    public boolean contieneMiembro(String correo) {
        NodoGrupo actual = cabeza;
        while (actual != null) {
            if (actual.usuario.getCorreo().equalsIgnoreCase(correo)) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public Optional<LocalDateTime> obtenerFechaIngreso(String correo) {
        NodoGrupo actual = cabeza;
        while (actual != null) {
            if (actual.usuario.getCorreo().equalsIgnoreCase(correo)) {
                return Optional.of(actual.fechaIngreso);
            }
            actual = actual.siguiente;
        }
        return Optional.empty();
    }

    public void limpiarLista() {
        cabeza = null;
        tamanio = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

}