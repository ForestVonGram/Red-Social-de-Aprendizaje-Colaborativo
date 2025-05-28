package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.Conversacion;
import com.uniquindio.redsocial.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConversacionRepository extends JpaRepository<Conversacion, Long> {

    @Query("""
            SELECT DISTINCT c FROM Conversacion c\s
            JOIN FETCH c.participantes p\s
            WHERE :usuario MEMBER OF c.participantes\s
            AND c.estaActiva = true\s
            ORDER BY c.ultimaActividad DESC
           \s""")
    List<Conversacion> findByParticipantesContains(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT c FROM Conversacion c\s
            WHERE c.estaActiva = true\s
            AND :usuario MEMBER OF c.participantes\s
            ORDER BY c.ultimaActividad DESC
           \s""")
    Page<Conversacion> findActiveConversations(
            @Param("usuario") Usuario usuario,
            Pageable pageable
    );

    @Query("""
            SELECT c FROM Conversacion c\s
            WHERE c.fechaCreacion >= :fecha\s
            AND :usuario MEMBER OF c.participantes\s
            ORDER BY c.fechaCreacion DESC
           \s""")
    List<Conversacion> findByFechaCreacionAfter(
            @Param("fecha") LocalDateTime fecha,
            @Param("usuario") Usuario usuario
    );

    @Query("""
            SELECT c FROM Conversacion c\s
            WHERE c.ultimaActividad < :fecha\s
            AND c.estaActiva = true
           \s""")
    List<Conversacion> findInactiveConversations(@Param("fecha") LocalDateTime fecha);

    @Modifying
    @Query("UPDATE Conversacion c SET c.estaActiva = false WHERE c.id = :id")
    void markAsInactive(@Param("id") Long id);

    @Query("""
            SELECT COUNT(c) FROM Conversacion c\s
            WHERE :usuario MEMBER OF c.participantes\s
            AND c.estaActiva = true
           \s""")
    long countActiveConversationsByUser(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT c FROM Conversacion c\s
            WHERE SIZE(c.participantes) > 2\s
            AND :usuario MEMBER OF c.participantes\s
            AND c.estaActiva = true
           \s""")
    List<Conversacion> findGroupConversations(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT c FROM Conversacion c\s
            WHERE SIZE(c.mensajes) >= :minMensajes\s
            AND c.estaActiva = true\s
            AND :usuario MEMBER OF c.participantes
           \s""")
    List<Conversacion> findByMinimumMessages(
            @Param("minMensajes") int minMensajes,
            @Param("usuario") Usuario usuario
    );

    @Query("""
            SELECT\s
                COUNT(c) as totalConversaciones,
                AVG(SIZE(c.mensajes)) as promedioMensajes,
                MAX(SIZE(c.participantes)) as maxParticipantes
            FROM Conversacion c\s
            WHERE c.estaActiva = true
           \s""")
    Object[] getConversationStatistics();

    @Modifying
    @Query("UPDATE Conversacion c SET c.ultimaActividad = :fecha WHERE c.id = :id")
    void updateUltimaActividad(
            @Param("id") Long id,
            @Param("fecha") LocalDateTime fecha
    );
}