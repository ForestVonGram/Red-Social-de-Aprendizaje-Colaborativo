package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.dto.RegisterDTO;
import com.uniquindio.redsocial.model.Conversacion;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario registrar(RegisterDTO dto) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByCorreo(dto.getCorreo());
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("El correo ya está en uso por otro usuario.");
        }

        Usuario usuario = new Usuario(
                dto.getNombre(),
                dto.getCorreo(),
                passwordEncoder.encode(dto.getContrasenia()), // Hash the password
                dto.getIntereses()
        );
        return usuarioRepository.save(usuario);
    }

    public boolean autenticar(String correo, String contrasenia) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
        return usuario.isPresent() && passwordEncoder.matches(contrasenia, usuario.get().getContrasenia());
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

    public Optional<Usuario> buscarPorCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es requerido");
        }
        return usuarioRepository.findByCorreo(correo);
    }

    public boolean verificarCorreoDisponible(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es requerido");
        }
        return buscarPorCorreo(correo).isEmpty();
    }

    public Page<Usuario> buscarUsuarios(String criterio, Pageable pageable) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return usuarioRepository.findAll(pageable);
        }

        return usuarioRepository.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(
                criterio.trim(),
                criterio.trim(),
                pageable
        );
    }
}
