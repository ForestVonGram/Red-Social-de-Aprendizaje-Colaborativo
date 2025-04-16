package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.dto.UsuarioDTO;
import com.uniquindio.redsocial.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public String registrar(@RequestBody UsuarioDTO dto) {
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
}

