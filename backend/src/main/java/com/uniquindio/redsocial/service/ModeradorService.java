package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModeradorService {
    private final UsuarioService usuarioService;
    private final ContenidoService contenidoService;

    public boolean eliminarUsuario(String correo) {
        return usuarioService.eliminarUsuario(correo);
    }

    public boolean eliminarContenido(String idContenido) {
        return contenidoService.eliminarContenido(idContenido);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    public List<Contenido> listarContenidos() {
        return contenidoService.listarContenidos();
    }
}
