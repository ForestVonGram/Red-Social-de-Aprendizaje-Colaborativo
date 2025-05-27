package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    List<Solicitud> findByResueltaFalseOrderByPrioridadDescFechaCreacionAsc();

    List<Solicitud> findByResueltaFalse();

    List<Solicitud> findByEstudianteOrderByFechaCreacionDesc(String estudiante);

    List<Solicitud> findByModeradorAsignadoIdOrderByPrioridadDescFechaCreacionAsc(Long moderadorId);

    boolean existsByEstudianteAndResueltaFalse(String estudiante);

    long countByModeradorAsignadoIdAndResueltaFalse(Long moderadorId);
}