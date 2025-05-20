package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.Conversacion;
import com.uniquindio.redsocial.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversacionRepository extends JpaRepository<Conversacion, Long>{
    List<Conversacion> findByParticipantesContains(Usuario usuario);
}
