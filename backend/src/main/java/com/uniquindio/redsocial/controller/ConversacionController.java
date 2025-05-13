package com.uniquindio.redsocial.controller;
import com.uniquindio.redsocial.service.ConversacionService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    public ResponseEntity
}
