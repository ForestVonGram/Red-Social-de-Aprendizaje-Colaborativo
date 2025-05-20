package com.uniquindio.redsocial.controller;


import com.uniquindio.redsocial.dto.MensajeDTO;
import com.uniquindio.redsocial.model.Mensaje;
import com.uniquindio.redsocial.service.MensajeService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
@RequiredArgsConstructor
public class MensajeController {
    private final MensajeService mensajeService;

    @PostMapping
    public ResponseEntity<Mensaje> enviarMensaje(@RequestBody MensajeDTO dto){
        return ResponseEntity.ok(mensajeService.enviarMensaje(dto.getRemitenteId(), dto.getConversacionId(), dto.getContenido()));
    }

    @GetMapping("/conversacion/{id}")
    public ResponseEntity<List<Mensaje>> obtenerMensajes(@PathVariable Long id){
        return ResponseEntity.ok(mensajeService.obtenerMensajesPorConversacion(id));
    }
}