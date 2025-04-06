package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.service.BusquedaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buscar")
public class BusquedaController {

    @Autowired
    private BusquedaService busquedaService;

    @GetMapping("/titulo")
    public List<Contenido> buscarPorTitulo(@RequestParam String titulo) {
        return busquedaService.buscarPorTitulo(titulo);
    }
}
