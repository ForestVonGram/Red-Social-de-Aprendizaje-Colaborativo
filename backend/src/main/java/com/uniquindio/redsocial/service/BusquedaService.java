package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.ArbolContenido;
import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.repository.ContenidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusquedaService {

    private final ArbolContenido arbol = new ArbolContenido();

    @Autowired
    public BusquedaService(ContenidoRepository contenidoRepository) {
        contenidoRepository.findAll().forEach(arbol::insertar);
    }

    public List<Contenido> buscarPorTitulo(String titulo) {
        return arbol.buscarPorTitulo(titulo);
    }
}
