package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.ArbolContenido;
import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.repository.ContenidoRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class BusquedaService {

    private final ContenidoRepository contenidoRepository;
    private final ArbolContenido arbol;

    @PostConstruct
    public void inicializarArbol() {
        try {
            List<Contenido> contenidos = contenidoRepository.findAll();
            contenidos.forEach(arbol::insertar);
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar el árbol de contenidos", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Contenido> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return Collections.emptyList();
        }

        try {
            return arbol.buscarPorTitulo(titulo.trim().toLowerCase());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar contenido por título: " + titulo, e);
        }
    }

    public void actualizarArbol(Contenido contenido) {
        try {
            arbol.insertar(contenido);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el árbol con nuevo contenido", e);
        }
    }

    public void eliminarDelArbol(Contenido contenido) {
        try {
            arbol.eliminar(contenido);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar contenido del árbol", e);
        }
    }
}