package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecomendacionService {

    private final UsuarioRepository usuarioRepository;

    private final GrafoUsuariosService grafoUsuariosService;

    public Set<Usuario> obtenerRecomendaciones(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        Set<Usuario> recomendacionesGrafo = grafoUsuariosService.recomendarAmigos(correo);
        Set<Usuario> recomendacionesIntereses = obtenerRecomendacionesPorIntereses(usuario);

        // Combinar y priorizar recomendaciones
        Set<Usuario> recomendacionesCombinadas = new HashSet<>();
        recomendacionesCombinadas.addAll(recomendacionesGrafo);
        recomendacionesCombinadas.addAll(recomendacionesIntereses);

        // Filtrar usuario actual y limitar cantidad
        return recomendacionesCombinadas.stream()
                .filter(u -> !u.getCorreo().equals(correo))
                .limit(10)
                .collect(Collectors.toSet());
    }

    private Set<Usuario> obtenerRecomendacionesPorIntereses(Usuario usuario) {
        return usuarioRepository.findByIntereses(
                        usuario.getIntereses(),
                        usuario.getId()
                ).stream()
                .filter(u -> !grafoUsuariosService.estanConectados(usuario.getCorreo(), u.getCorreo()))
                .collect(Collectors.toSet());
    }

    public void registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        grafoUsuariosService.agregarUsuario(usuario);
    }

    public void conectar(String correo1, String correo2) {
        if (correo1 == null || correo2 == null ||
                correo1.trim().isEmpty() || correo2.trim().isEmpty()) {
            throw new IllegalArgumentException("Los correos no pueden estar vacíos");
        }

        if (correo1.equals(correo2)) {
            throw new IllegalArgumentException("Un usuario no puede conectarse consigo mismo");
        }

        grafoUsuariosService.agregarConexion(correo1, correo2);
    }

    public boolean verificarConexion(String correo1, String correo2) {
        if (correo1 == null || correo2 == null ||
                correo1.trim().isEmpty() || correo2.trim().isEmpty()) {
            throw new IllegalArgumentException("Los correos no pueden estar vacíos");
        }

        return grafoUsuariosService.estanConectados(correo1, correo2);
    }

    public void eliminarConexion(String correo1, String correo2) {
        if (correo1 == null || correo2 == null ||
                correo1.trim().isEmpty() || correo2.trim().isEmpty()) {
            throw new IllegalArgumentException("Los correos no pueden estar vacíos");
        }

        grafoUsuariosService.eliminarConexion(correo1, correo2);
    }

    public Set<Usuario> obtenerAmigos(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }

        return grafoUsuariosService.obtenerAmigos(correo);
    }
}