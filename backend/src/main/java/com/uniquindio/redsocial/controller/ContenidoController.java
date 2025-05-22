package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.service.ContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contenido")
public class ContenidoController {

    @Autowired
    private ContenidoService contenidoService;

    @PostMapping("/publicar")
    public Contenido publicar(
            @RequestParam Long idUsuario,
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam String tipo,
            @RequestParam String url) {
        return contenidoService.publicar(idUsuario, titulo, descripcion, tipo, url);
    }

    @GetMapping("/mas-valorados")
    public List<Contenido> obtenerContenidosMasValorados() {
        return contenidoService.obtenerContenidosMasValorados();
    }
}
