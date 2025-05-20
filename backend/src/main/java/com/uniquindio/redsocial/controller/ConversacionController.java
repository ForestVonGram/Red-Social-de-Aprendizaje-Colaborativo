package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Conversacion;
import com.uniquindio.redsocial.service.ConversacionService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/conversaciones")
@RequiredArgsConstructor
public class ConversacionController {

    private final ConversacionService conversacionService;

    @PostMapping
    public ResponseEntity<Conversacion> crearConversacion(@RequestBody List<String> idsUsuarios){
        return ResponseEntity.ok(conversacionService.crearConversacion(idsUsuarios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conversacion> obtenerConversacion(@PathVariable Long id){
        return conversacionService.obtenerConversacion(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
