package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findById(Long id);

    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
}

