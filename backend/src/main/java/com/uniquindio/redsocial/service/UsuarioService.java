package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.dto.UsuarioDTO;
import com.uniquindio.redsocial.model.Conversacion;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario registrar(UsuarioDTO dto) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByCorreo(dto.getCorreo());
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("El correo ya está en uso por otro usuario.");
        }

        Usuario usuario = new Usuario(
                null,
                dto.getNombre(),
                dto.getCorreo(),
                dto.getContrasenia(),
                dto.getIntereses(),
                dto.getConversaciones()
        );
        return usuarioRepository.save(usuario);
    }

    public boolean autenticar(String correo, String contrasenia) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
        return usuario.isPresent() && usuario.get().getContrasenia().equals(contrasenia);
    }

    public boolean eliminarUsuario(String correo) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
        if (usuario.isPresent()) {
            usuarioRepository.delete(usuario.get());
            return true;
        }
        return false;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<Conversacion> obtenerConversaciones(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado con el correo proporcionado.");
        }
        return usuario.get().getConversaciones();
    }
}