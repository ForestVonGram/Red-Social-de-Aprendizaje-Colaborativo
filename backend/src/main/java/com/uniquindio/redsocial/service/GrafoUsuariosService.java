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
}