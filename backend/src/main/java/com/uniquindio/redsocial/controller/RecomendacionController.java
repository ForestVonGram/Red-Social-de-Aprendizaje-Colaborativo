package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.RecomendacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/recomendaciones")
@RequiredArgsConstructor
public class RecomendacionController {
    private final RecomendacionService recomendacionService;

    @PostMapping("/registrar")
    public void registrarUsuario(@RequestBody Usuario usuario) {
        recomendacionService.registrarUsuario(usuario);
    }

    @PostMapping("/conectar")
    public void conectar(@RequestParam String correo1, @RequestParam String correo2) {
        recomendacionService.conectar(correo1, correo2);
    }

    @GetMapping("/{correo}")
    public Set<Usuario> recomendar(@PathVariable String correo) {
        return recomendacionService.obtenerRecomendaciones(correo);
    }
}