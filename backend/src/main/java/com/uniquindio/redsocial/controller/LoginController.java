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
    public String login(@RequestBody LoginDTO loginDTO) {
        boolean autenticado = usuarioService.autenticar(loginDTO.getCorreo(), loginDTO.getPassword());

        if (autenticado) {
            // Puedes devolver información del usuario o un token más adelante
            return "Login exitoso";
        } else {
            return "Correo o contraseña incorrectos";
        }
    }

    @GetMapping("/{correo}")
    public Usuario obtenerUsuario(@PathVariable String correo) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
        return usuario.orElse(null);
    }
}
