package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.dto.LoginDTO;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import com.uniquindio.redsocial.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*") // Permite solicitudes desde cualquier origen (útil para desarrollo local con React)
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public boolean login(@RequestBody LoginDTO loginDTO) {
        System.err.println("LoginController.login" + loginDTO.getPassword() + loginDTO.getCorreo());
        return usuarioService.autenticar(loginDTO.getCorreo(), loginDTO.getPassword());
    }

    @GetMapping("/{correo}")
    public Usuario obtenerUsuario(@PathVariable String correo) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
        return usuario.orElse(null);
    }
}
