package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.ListaGrupos;
import com.uniquindio.redsocial.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoService {
    private ListaGrupos listaGrupos = new ListaGrupos();

    public void asignarUsuarioAGrupo(Usuario usuario) {
        listaGrupos.agregarUsuario(usuario);
    }

    public void eliminarUsuarioDelGrupo(String correo) {
        listaGrupos.eliminarUsuarioPorCorreo(correo);
    }

    public List<Usuario> listarGrupo() {
        return listaGrupos.toList();
    }
}
