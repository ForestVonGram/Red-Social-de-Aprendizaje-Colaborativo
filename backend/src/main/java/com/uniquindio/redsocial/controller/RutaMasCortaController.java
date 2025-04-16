package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.dto.RutaMasCortaDTO;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.GrafoUsuariosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rutas")
public class RutaMasCortaController {
    private final GrafoUsuariosService grafoUsuariosService;

    public RutaMasCortaController(GrafoUsuariosService grafoUsuariosService) {
        this.grafoUsuariosService = grafoUsuariosService;
    }

    @PostMapping("/buscar")
    public ResponseEntity<List<String>> obtenerRutaMasCorta(@RequestBody RutaMasCortaDTO dto) {
        List<Usuario> ruta = grafoUsuariosService.buscarRutaMasCorta(dto.getCorreoOrigen(), dto.getCorreoDestino());
        if (ruta.isEmpty()) {
            return ResponseEntity.noContent().build(); // En caso de no haber ruta
        }
        List<String> correosRuta = ruta.stream().map(Usuario::getCorreo).toList();
        return ResponseEntity.ok(correosRuta);
    }
}