package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.ModeradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moderador")
@RequiredArgsConstructor
public class ModeradorController {
    private final ModeradorService moderadorService;

    @DeleteMapping("/eliminar-usuario/{correo}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable String correo) {
        if (moderadorService.eliminarUsuario(correo)) {
            return ResponseEntity.ok("Usuario eliminado correctamente");
        }
        return ResponseEntity.badRequest().body("No se pudo eliminar al usuario.");
    }

    @DeleteMapping("/eliminar-contenido/{id}")
    public ResponseEntity<String> eliminarContenido(@PathVariable String id) {
        if (moderadorService.eliminarContenido(id)) {
            return ResponseEntity.ok("Contenido eliminado correctamente");
        }
        return ResponseEntity.badRequest().body("No se pudo eliminar el contenido.");
    }

    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return moderadorService.listarUsuarios();
    }

    @GetMapping("/contenidos")
    public List<Contenido> listarContenidos() {
        return moderadorService.listarContenidos();
    }
}
