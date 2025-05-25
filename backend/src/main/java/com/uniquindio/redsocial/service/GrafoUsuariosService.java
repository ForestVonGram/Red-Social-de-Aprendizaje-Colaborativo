package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.GrafoUsuarios;
import com.uniquindio.redsocial.estructuras.GrafoVisualizador;
import com.uniquindio.redsocial.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GrafoUsuariosService {
    private final GrafoUsuarios grafoUsuarios;

    public GrafoUsuariosService(GrafoUsuarios grafoUsuarios) {
        this.grafoUsuarios = grafoUsuarios;
    }

    public void agregarUsuario(Usuario usuario) {
        grafoUsuarios.agregarUsuario(usuario);
    }

    public void agregarConexion(String correo1, String correo2) {
        grafoUsuarios.conectarUsuarios(correo1, correo2);
    }

    public void eliminarConexion(String correo1, String correo2) {
        grafoUsuarios.eliminarConexion(correo1, correo2);
    }

    public boolean estanConectados(String correo1, String correo2) {
        return grafoUsuarios.estanConectados(correo1, correo2);
    }

    public Set<Usuario> obtenerAmigos(String correo) {
        return grafoUsuarios.obtenerAmigos(correo);
    }

    public Set<Usuario> recomendarAmigos(String correo) {
        return grafoUsuarios.recomendarAmigos(correo);
    }

    public List<Usuario> buscarRutaMasCorta(String correoOrigen, String correoDestino) {
        return grafoUsuarios.buscarRutaMasCorta(correoOrigen, correoDestino);
    }

    public List<Usuario> obtenerUsuariosConMasConexiones(int top) {
        return grafoUsuarios.obtenerUsuariosConMasConexiones(top);
    }

    public List<Usuario> sugerirCompanerosPorIntereses(String correoUsuario) {
        Usuario usuario = grafoUsuarios.getUsuario(correoUsuario);
        if (usuario == null) return Collections.emptyList();

        Set<Usuario> amigos = grafoUsuarios.obtenerAmigos(correoUsuario);
        Set<Usuario> conectados = grafoUsuarios.obtenerUsuariosConectadosIndirectamente(correoUsuario);

        List<Usuario> sugerencias = new ArrayList<>();

        for (Usuario candidato : conectados) {
            if (!amigos.contains(candidato) &&
                    !candidato.getCorreo().equals(correoUsuario) &&
                    tienenInteresesEnComun(usuario, candidato)) {
                sugerencias.add(candidato);
            }
        }

        sugerencias.sort((a, b) ->
                Integer.compare(contarInteresesEnComun(usuario, b), contarInteresesEnComun(usuario, a))
        );

        return sugerencias;
    }

    private boolean tienenInteresesEnComun(Usuario u1, Usuario u2) {
        for (String interes : u1.getIntereses()) {
            if (u2.getIntereses().contains(interes)) return true;
        }
        return false;
    }

    private int contarInteresesEnComun(Usuario u1, Usuario u2) {
        int contador = 0;
        for (String interes : u1.getIntereses()) {
            if (u2.getIntereses().contains(interes)) contador++;
        }
        return contador;
    }

    public void visualizarGrafo() {
        GrafoVisualizador.mostrarGrafo(grafoUsuarios);
    }

    public Map<String, Set<String>> obtenerTodasLasConexiones() {
        return grafoUsuarios.getConexiones();
    }

    public Collection<Usuario> obtenerTodosLosUsuarios() {
        return grafoUsuarios.getUsuarios().values();
    }
}