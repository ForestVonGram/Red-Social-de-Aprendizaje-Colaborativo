package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.repository.ContenidoRepository;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class ContenidoService {

    private ContenidoRepository contenidoRepository;

    private UsuarioRepository usuarioRepository;

    public Contenido publicar(Long idUsuario, String titulo, String descripcion, String tipo, String url) {
        Usuario autor = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Contenido contenido = new Contenido();
        contenido.setTitulo(titulo);
        contenido.setDescripcion(descripcion);
        contenido.setTipo(Contenido.TipoContenido.valueOf(tipo.toUpperCase()));        contenido.setUrl(url);
        contenido.setAutor(autor);
        contenido.setFechaPublicacion(LocalDateTime.now());
        contenido.setValoraciones(new ArrayList<>());

        return contenidoRepository.save(contenido);
    }

    public boolean eliminarContenido(Long id) {
        if (contenidoRepository.existsById(id)) {
            contenidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Contenido> listarContenidos() {
        return contenidoRepository.findAll();
    }

    public List<Contenido> obtenerContenidosMasValorados() {
        return contenidoRepository.findMostValuedContents(PageRequest.of(0, 10))
                .getContent();
    }

    public Contenido actualizarContenido(Long id, String titulo, String descripcion) {
        Contenido contenido = contenidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contenido no encontrado"));

        contenido.setTitulo(titulo);
        contenido.setDescripcion(descripcion);

        return contenidoRepository.save(contenido);
    }

    public Page<Contenido> buscarContenidos(String criterio, Pageable pageable) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return contenidoRepository.findAll(pageable);
        }
        return contenidoRepository.searchByKeyword(criterio.trim(), pageable);
    }
}