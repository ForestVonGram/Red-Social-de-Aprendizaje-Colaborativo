package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.Mensaje;
import com.uniquindio.redsocial.model.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, String>{
    List<Mensaje> findByConversacionOrderByFechaAsc(Conversacion conversacion);
}
