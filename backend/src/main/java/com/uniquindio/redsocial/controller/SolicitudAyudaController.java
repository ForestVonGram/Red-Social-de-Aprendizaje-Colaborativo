package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.SolicitudAyuda;
import com.uniquindio.redsocial.service.SolicitudAyudaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ayuda")
@RequiredArgsConstructor
public class SolicitudAyudaController {
    private final SolicitudAyudaService ayudaService;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarSolicitud(@RequestParam String correo,
                                                     @RequestParam String descripcion,
                                                     @RequestParam SolicitudAyuda.Prioridad prioridad) {
        ayudaService.registrarSolicitud(correo, descripcion, prioridad);
        return ResponseEntity.ok("Solicitud registrada con éxito");
    }

    @GetMapping("/atender")
    public ResponseEntity<SolicitudAyuda> atenderSolicitud() {
        SolicitudAyuda solicitud = ayudaService.atenderSiguiente();
        return (solicitud != null) ? ResponseEntity.ok(solicitud) : ResponseEntity.noContent().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<SolicitudAyuda>> listarSolicitudes() {
        return ResponseEntity.ok(ayudaService.listarSolicitudesOrdenadas());
    }
}