package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Solicitud;
import com.uniquindio.redsocial.service.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {
    private final SolicitudService solicitudService;

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarSolicitud(@RequestParam String estudiante,
                                                   @RequestParam String descripcion,
                                                   @RequestParam int prioridad) {
        solicitudService.agregarSolicitud(estudiante, descripcion, prioridad);
        return ResponseEntity.ok("Solicitud agregada con exito");
    }

    @GetMapping("/atender")
    public ResponseEntity<Solicitud> atenderSolicitud() {
        Solicitud solicitud = solicitudService.atenderSolicitud();
        return (solicitud != null) ? ResponseEntity.ok(solicitud) : ResponseEntity.notFound().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<String>> listarSolicitudes() {
        return ResponseEntity.ok(solicitudService.getSolicitudesOrdenadas());
    }
}
