package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.SolicitudAyuda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SolicitudAyudaRepository extends JpaRepository<SolicitudAyuda, Long> {

    List<SolicitudAyuda> findByResueltaFalseOrderByPrioridadAscFechaCreacionAsc();

    List<SolicitudAyuda> findByCorreoEstudianteOrderByFechaCreacionDesc(String correoEstudiante);

    List<SolicitudAyuda> findByModeradorAsignadoIdOrderByPrioridadAscFechaCreacionAsc(Long moderadorId);

    @Query("""
            SELECT s FROM SolicitudAyuda s\s
            WHERE s.resuelta = false\s
            AND s.moderadorAsignado IS NULL\s
            ORDER BY s.prioridad ASC, s.fechaCreacion ASC
           \s""")
    List<SolicitudAyuda> findPendingUnassigned();

    @Query("""
            SELECT s FROM SolicitudAyuda s\s
            WHERE LOWER(s.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%'))\s
            ORDER BY s.fechaCreacion DESC
           \s""")
    Page<SolicitudAyuda> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
            SELECT COUNT(s) FROM SolicitudAyuda s\s
            WHERE s.resuelta = false\s
            AND s.fechaCreacion <= :fecha
           \s""")
    long countPendingOlderThan(@Param("fecha") LocalDateTime fecha);

    @Query("""
            SELECT s FROM SolicitudAyuda s\s
            WHERE s.resuelta = true\s
            AND s.moderadorAsignado.id = :moderadorId\s
            AND s.fechaResolucion BETWEEN :inicio AND :fin
           \s""")
    List<SolicitudAyuda> findResueltasByModeradorAndFecha(
            @Param("moderadorId") Long moderadorId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );


    boolean existsByCorreoEstudianteAndResueltaFalse(String correoEstudiante);

    long countByModeradorAsignadoIdAndResueltaFalse(Long moderadorId);
}