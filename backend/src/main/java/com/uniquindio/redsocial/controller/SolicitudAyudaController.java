package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.dto.SolicitudAyudaDTO;
import com.uniquindio.redsocial.model.SolicitudAyuda;
import com.uniquindio.redsocial.service.SolicitudAyudaService;
import lombok.Data;
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
    public ResponseEntity<String> registrarSolicitud(@RequestBody SolicitudAyudaDTO dto) {
        ayudaService.registrarSolicitud(dto.getCorreoEstudiante(), dto.getDescripcion(), dto.getPrioridad());
        return ResponseEntity.ok("Solicitud registrada");
    }

    @GetMapping("/atender")
    public ResponseEntity<SolicitudAyuda> atenderSolicitud() {
        return ResponseEntity.ok(ayudaService.atenderSiguiente());
    }

    @GetMapping("/listar")
    public ResponseEntity<List<SolicitudAyuda>> listarSolicitudes() {
        return ResponseEntity.ok(ayudaService.listarSolicitudes());
    }
}
