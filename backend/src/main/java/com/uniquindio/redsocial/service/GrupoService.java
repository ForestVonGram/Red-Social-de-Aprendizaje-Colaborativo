package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.ListaGrupos;
import com.uniquindio.redsocial.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GrupoService {
    private final ListaGrupos listaGrupos = new ListaGrupos();

    public void asignarUsuarioAGrupo(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo del usuario es requerido");
        }

        Optional<Usuario> usuarioExistente = buscarUsuario(usuario.getCorreo());
        if (usuarioExistente.isPresent()) {
            throw new IllegalStateException("El usuario ya existe en el grupo");
        }

        listaGrupos.agregarUsuario(usuario);
    }

    public boolean eliminarUsuarioDelGrupo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es requerido");
        }

        Optional<Usuario> usuario = buscarUsuario(correo);
        if (usuario.isEmpty()) {
            return false;
        }

        listaGrupos.eliminarUsuarioPorCorreo(correo);
        return true;
    }

    public List<Usuario> listarGrupo() {
        return listaGrupos.toList();
    }

    public Optional<Usuario> buscarUsuario(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es requerido");
        }

        return listaGrupos.toList().stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }

    public boolean existeUsuario(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es requerido");
        }

        return buscarUsuario(correo).isPresent();
    }
}