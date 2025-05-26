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
import java.util.Optional;

@Repository
public interface ConversacionRepository extends JpaRepository<Conversacion, Long> {

    @Query("""
            SELECT DISTINCT c FROM Conversacion c 
            JOIN FETCH c.participantes p 
            WHERE :usuario MEMBER OF c.participantes 
            AND c.estaActiva = true 
            ORDER BY c.ultimaActividad DESC
            """)
    List<Conversacion> findByParticipantesContains(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT c FROM Conversacion c 
            WHERE c.participantes.size = 2 
            AND :usuario1 MEMBER OF c.participantes 
            AND :usuario2 MEMBER OF c.participantes 
            AND c.estaActiva = true
            """)
    Optional<Conversacion> findConversacionBetweenUsers(
            @Param("usuario1") Usuario usuario1,
            @Param("usuario2") Usuario usuario2
    );

    @Query("""
            SELECT c FROM Conversacion c 
            WHERE c.estaActiva = true 
            AND :usuario MEMBER OF c.participantes 
            ORDER BY c.ultimaActividad DESC
            """)
    Page<Conversacion> findActiveConversations(
            @Param("usuario") Usuario usuario,
            Pageable pageable
    );

    @Query("""
            SELECT c FROM Conversacion c 
            WHERE c.fechaCreacion >= :fecha 
            AND :usuario MEMBER OF c.participantes 
            ORDER BY c.fechaCreacion DESC
            """)
    List<Conversacion> findByFechaCreacionAfter(
            @Param("fecha") LocalDateTime fecha,
            @Param("usuario") Usuario usuario
    );

    @Query("""
            SELECT c FROM Conversacion c 
            WHERE c.ultimaActividad < :fecha 
            AND c.estaActiva = true
            """)
    List<Conversacion> findInactiveConversations(@Param("fecha") LocalDateTime fecha);

    @Modifying
    @Query("UPDATE Conversacion c SET c.estaActiva = false WHERE c.id = :id")
    void markAsInactive(@Param("id") Long id);

    @Query("""
            SELECT COUNT(c) FROM Conversacion c 
            WHERE :usuario MEMBER OF c.participantes 
            AND c.estaActiva = true
            """)
    long countActiveConversationsByUser(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT c FROM Conversacion c 
            WHERE SIZE(c.participantes) > 2 
            AND :usuario MEMBER OF c.participantes 
            AND c.estaActiva = true
            """)
    List<Conversacion> findGroupConversations(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT c FROM Conversacion c 
            WHERE SIZE(c.mensajes) >= :minMensajes 
            AND c.estaActiva = true 
            AND :usuario MEMBER OF c.participantes
            """)
    List<Conversacion> findByMinimumMessages(
            @Param("minMensajes") int minMensajes,
            @Param("usuario") Usuario usuario
    );

    @Query("""
            SELECT 
                COUNT(c) as totalConversaciones,
                AVG(SIZE(c.mensajes)) as promedioMensajes,
                MAX(SIZE(c.participantes)) as maxParticipantes
            FROM Conversacion c 
            WHERE c.estaActiva = true
            """)
    Object[] getConversationStatistics();

    @Query("""
            SELECT c FROM Conversacion c 
            WHERE c.participantes IN :participantes 
            AND SIZE(c.participantes) = :numParticipantes 
            AND c.estaActiva = true
            """)
    List<Conversacion> findByExactParticipants(
            @Param("participantes") List<Usuario> participantes,
            @Param("numParticipantes") int numParticipantes
    );

    @Modifying
    @Query("UPDATE Conversacion c SET c.ultimaActividad = :fecha WHERE c.id = :id")
    void updateUltimaActividad(
            @Param("id") Long id,
            @Param("fecha") LocalDateTime fecha
    );
}