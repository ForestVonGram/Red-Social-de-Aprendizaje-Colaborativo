package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.exception.ModeradorException;
import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.model.Moderador;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.repository.ModeradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ModeradorService {

    private final UsuarioService usuarioService;

    private final ContenidoService contenidoService;

    private final ModeradorRepository moderadorRepository;

    @PreAuthorize("hasRole('MODERADOR')")
    public boolean eliminarUsuario(String correo) {
        validarCorreo(correo);
        registrarAccion("Eliminación de usuario: " + correo);
        return usuarioService.eliminarUsuario(correo);
    }

    @PreAuthorize("hasRole('MODERADOR')")
    public boolean eliminarContenido(String idContenido) {
        validarId(idContenido);
        registrarAccion("Eliminación de contenido: " + idContenido);
        try {
            Long id = Long.parseLong(idContenido);
            return contenidoService.eliminarContenido(id);
        } catch (NumberFormatException e) {
            throw new ModeradorException("El ID del contenido debe ser un número válido");
        }
    }

    @PreAuthorize("hasRole('MODERADOR')")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PreAuthorize("hasRole('MODERADOR')")
    public List<Contenido> listarContenidos() {
        return contenidoService.listarContenidos();
    }

    @PreAuthorize("hasRole('MODERADOR')")
    public Page<Usuario> buscarUsuarios(String criterio, Pageable pageable) {
        return usuarioService.buscarUsuarios(criterio, pageable);
    }

    @PreAuthorize("hasRole('MODERADOR')")
    public Page<Contenido> buscarContenidos(String criterio, Pageable pageable) {
        return contenidoService.buscarContenidos(criterio, pageable);
    }


    @PreAuthorize("hasRole('MODERADOR')")
    public Optional<Usuario> obtenerDetallesUsuario(String correo) {
        validarCorreo(correo);
        return usuarioService.buscarPorCorreo(correo);
    }

    private void validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new ModeradorException("El correo no puede estar vacío");
        }
        if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ModeradorException("Formato de correo inválido");
        }
    }

    private void validarId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ModeradorException("El ID no puede estar vacío");
        }
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new ModeradorException("El ID debe ser un número válido");
        }
    }

    private void registrarAccion(String descripcion) {
        try {
            Moderador moderador = obtenerModeradorActual();
            assert moderador != null;
            moderador.agregarAccion(descripcion, LocalDateTime.now());
            moderadorRepository.save(moderador);
        } catch (Exception e) {
            throw new ModeradorException("Error al registrar la acción del moderador: " + e.getMessage());
        }
    }

    private Moderador obtenerModeradorActual() {
        return null;
    }
}