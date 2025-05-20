package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.dto.UsuarioDTO;
import com.uniquindio.redsocial.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "*")
public class RegisterController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public String registrar(@RequestBody UsuarioDTO dto) {
        usuarioService.registrar(dto);
        return "Usuario registrado correctamente";
    }
}
