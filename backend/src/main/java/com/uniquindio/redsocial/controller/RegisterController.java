package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.dto.RegisterDTO;
import com.uniquindio.redsocial.dto.UsuarioDTO;
import com.uniquindio.redsocial.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public String registrar(@RequestBody RegisterDTO dto) {
        usuarioService.registrar(dto);
        return "Usuario registrado correctamente";
    }
}
