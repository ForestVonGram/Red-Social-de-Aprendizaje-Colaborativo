package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.ListaGrupos;
import com.uniquindio.redsocial.model.GrupoEstudio;
import com.uniquindio.redsocial.model.Usuario;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GrupoService {
    @Autowired
    private final ListaGrupos listaGrupos;

    @Autowired
    private final GrupoEstudioService grupoEstudioService;

    @Getter
    private final GrupoEstudio grupoActual;

    public GrupoService() {
        this.listaGrupos = new ListaGrupos();
        this.grupoEstudioService = new GrupoEstudioService();
        this.grupoActual = new GrupoEstudio();
        this.grupoActual.setTemaCentral("Grupo Default");
    }

    public void asignarUsuarioAGrupo(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo del usuario es requerido");
        }

        // Verificar si el usuario está registrado en el sistema de grupos de estudio
        try {
            grupoEstudioService.obtenerUsuario(usuario.getCorreo());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("El usuario debe estar registrado en el sistema de grupos de estudio");
        }

        Optional<Usuario> usuarioExistente = buscarUsuario(usuario.getCorreo());
        if (usuarioExistente.isPresent()) {
            throw new IllegalStateException("El usuario ya existe en el grupo");
        }

        if (!listaGrupos.agregarUsuario(usuario, grupoActual)) {
            throw new IllegalStateException("No se pudo agregar el usuario al grupo");
        }
    }

    public boolean eliminarUsuarioDelGrupo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es requerido");
        }

        Optional<Usuario> usuario = buscarUsuario(correo);
        if (usuario.isEmpty()) {
            return false;
        }

        return listaGrupos.eliminarUsuarioPorCorreo(correo);
    }

    public List<Usuario> listarGrupo() {
        return listaGrupos.obtenerMiembrosPorGrupo(grupoActual);
    }

    public Optional<Usuario> buscarUsuario(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es requerido");
        }

        return listaGrupos.obtenerMiembrosPorGrupo(grupoActual).stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }

    public boolean existeUsuario(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es requerido");
        }

        return listaGrupos.contieneMiembro(correo);
    }

    public int getCantidadUsuarios() {
        return listaGrupos.getTamanio();
    }
}