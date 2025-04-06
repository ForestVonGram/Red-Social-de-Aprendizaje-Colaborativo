package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByCorreo(String correo);
}

