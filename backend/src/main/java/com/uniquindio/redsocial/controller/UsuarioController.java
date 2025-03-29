package com.uniquindio.redsocial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok("Usuario registrado con éxito.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String correo, @RequestParam String contrasenia) {
        boolean valido = usuarioService.verificarCredenciales(correo, contrasenia);
        if (valido) {
            return ResponseEntity.ok("Inicio de sesión exitoso.");
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectos.");
        }
    }
}
