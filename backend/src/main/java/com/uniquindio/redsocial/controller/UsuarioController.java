package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.dto.RegisterDTO;
import com.uniquindio.redsocial.dto.UsuarioDTO;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public String registrar(@RequestBody RegisterDTO dto) {
        usuarioService.registrar(dto);
        return "Usuario registrado correctamente";
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO dto) {
        if (usuarioService.autenticar(dto.getCorreo(), dto.getContrasenia())) {
            return "Inicio de sesión exitoso";
        } else {
            return "Credenciales incorrectas";
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }
}