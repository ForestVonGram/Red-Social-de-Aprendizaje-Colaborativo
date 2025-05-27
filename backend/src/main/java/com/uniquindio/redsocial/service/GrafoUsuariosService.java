package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.GrafoUsuarios;
import com.uniquindio.redsocial.estructuras.GrafoVisualizador;
import com.uniquindio.redsocial.model.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GrafoUsuariosService {
    private final GrafoUsuarios grafoUsuarios;
    private final GrafoVisualizador grafoVisualizador;
    private final UsuarioService usuarioService;

    public GrafoUsuariosService(GrafoUsuarios grafoUsuarios,
                                GrafoVisualizador grafoVisualizador,
                                UsuarioService usuarioService) {
        this.grafoUsuarios = grafoUsuarios;
        this.grafoVisualizador = grafoVisualizador;
        this.usuarioService = usuarioService;
    }


    public void agregarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        grafoUsuarios.agregarUsuario(usuario);
        log.info("Usuario agregado al grafo: {}", usuario.getCorreo());
    }

    public void agregarConexion(String correo1, String correo2) {
        validarCorreos(correo1, correo2);
        verificarUsuariosExisten(correo1, correo2);

        grafoUsuarios.conectarUsuarios(correo1, correo2);
        log.info("Conexión establecida entre {} y {}", correo1, correo2);
    }

    public void eliminarConexion(String correo1, String correo2) {
        validarCorreos(correo1, correo2);
        verificarUsuariosExisten(correo1, correo2);

        grafoUsuarios.eliminarConexion(correo1, correo2);
        log.info("Conexión eliminada entre {} y {}", correo1, correo2);
    }

    public boolean estanConectados(String correo1, String correo2) {
        validarCorreos(correo1, correo2);
        verificarUsuariosExisten(correo1, correo2);

        return grafoUsuarios.estanConectados(correo1, correo2);
    }

    public Set<Usuario> obtenerAmigos(String correo) {
        validarCorreo(correo);
        verificarUsuarioExiste(correo);

        return grafoUsuarios.obtenerAmigos(correo);
    }

    public Set<Usuario> recomendarAmigos(String correo) {
        validarCorreo(correo);
        verificarUsuarioExiste(correo);

        return grafoUsuarios.recomendarAmigos(correo);
    }

    public List<Usuario> buscarRutaMasCorta(String correoOrigen, String correoDestino) {
        validarCorreos(correoOrigen, correoDestino);
        verificarUsuariosExisten(correoOrigen, correoDestino);

        List<Usuario> ruta = grafoUsuarios.buscarRutaMasCorta(correoOrigen, correoDestino);
        if (ruta.isEmpty()) {
            log.info("No se encontró ruta entre {} y {}", correoOrigen, correoDestino);
        }
        return ruta;
    }

    public List<Usuario> obtenerUsuariosConMasConexiones(int top) {
        if (top <= 0) {
            throw new IllegalArgumentException("El número de usuarios debe ser positivo");
        }
        return grafoUsuarios.obtenerUsuariosConMasConexiones(top);
    }

    public List<Usuario> sugerirCompanerosPorIntereses(String correoUsuario) {
        validarCorreo(correoUsuario);
        verificarUsuarioExiste(correoUsuario);

        Usuario usuario = grafoUsuarios.getUsuario(correoUsuario);
        Set<Usuario> amigos = grafoUsuarios.obtenerAmigos(correoUsuario);
        Set<Usuario> conectados = grafoUsuarios.obtenerUsuariosConectadosIndirectamente(correoUsuario);

        return conectados.stream()
                .filter(candidato -> !amigos.contains(candidato) &&
                        !candidato.getCorreo().equals(correoUsuario) &&
                        tienenInteresesEnComun(usuario, candidato))
                .sorted(Comparator.comparingInt(candidato ->
                        -contarInteresesEnComun(usuario, candidato)))
                .collect(Collectors.toList());
    }

    private boolean tienenInteresesEnComun(Usuario u1, Usuario u2) {
        return !Collections.disjoint(u1.getIntereses(), u2.getIntereses());
    }

    private int contarInteresesEnComun(Usuario u1, Usuario u2) {
        return (int) u1.getIntereses().stream()
                .filter(u2.getIntereses()::contains)
                .count();
    }

    private void validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede ser nulo o vacío");
        }
    }

    private void validarCorreos(String correo1, String correo2) {
        validarCorreo(correo1);
        validarCorreo(correo2);
        if (correo1.equals(correo2)) {
            throw new IllegalArgumentException("Los correos no pueden ser iguales");
        }
    }

    private void verificarUsuarioExiste(String correo) {
        if (usuarioService.buscarPorCorreo(correo).isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado: " + correo);
        }
    }

    private void verificarUsuariosExisten(String correo1, String correo2) {
        verificarUsuarioExiste(correo1);
        verificarUsuarioExiste(correo2);
    }

    public void visualizarGrafo() {
        grafoVisualizador.mostrarGrafo(grafoUsuarios);
    }

    public Map<String, Set<String>> obtenerTodasLasConexiones() {
        return new HashMap<>(grafoUsuarios.getConexiones());
    }

    public Collection<Usuario> obtenerTodosLosUsuarios() {
        return new ArrayList<>(grafoUsuarios.getUsuarios().values());
    }
}