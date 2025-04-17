package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.repository.ContenidoRepository;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContenidoService {

    private final Map<String, Contenido> contenidos = new HashMap<>();

    @Autowired
    private ContenidoRepository contenidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Contenido publicar(String idUsuario, String titulo, String descripcion, String tipo, String url) {
        Usuario autor = usuarioRepository.findById(idUsuario).orElseThrow();
        Contenido contenido = new Contenido(
                UUID.randomUUID().toString(),
                titulo,
                descripcion,
                tipo,
                url,
                autor,
                new Date(),
                null
        );
        return contenidoRepository.save(contenido);
    }

    public boolean eliminarContenido(String id) {
        return contenidos.remove(id) != null;
    }

    public List<Contenido> listarContenidos() {
        return new ArrayList<>(contenidos.values());
    }
}
