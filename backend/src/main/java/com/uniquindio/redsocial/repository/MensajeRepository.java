package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.Conversacion;
import com.uniquindio.redsocial.model.Mensaje;
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
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    List<Mensaje> findByConversacionOrderByFechaEnvioAsc(Conversacion conversacion);

    @Query("""
            SELECT m FROM Mensaje m 
            WHERE m.conversacion = :conversacion 
            ORDER BY m.fechaEnvio DESC
            """)
    Page<Mensaje> findByConversacion(
            @Param("conversacion") Conversacion conversacion,
            Pageable pageable
    );

    @Query("""
            SELECT m FROM Mensaje m 
            WHERE m.conversacion IN (
                SELECT c FROM Conversacion c 
                WHERE :usuario MEMBER OF c.participantes
            ) 
            AND m.emisor != :usuario 
            AND m.leido = false 
            ORDER BY m.fechaEnvio DESC
            """)
    List<Mensaje> findUnreadMessagesByUser(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT COUNT(m) FROM Mensaje m 
            WHERE m.conversacion = :conversacion 
            AND m.emisor != :usuario 
            AND m.leido = false
            """)
    long countUnreadMessages(
            @Param("conversacion") Conversacion conversacion,
            @Param("usuario") Usuario usuario
    );

    @Modifying
    @Query("""
            UPDATE Mensaje m 
            SET m.leido = true 
            WHERE m.conversacion = :conversacion 
            AND m.emisor != :usuario 
            AND m.leido = false
            """)
    void markMessagesAsRead(
            @Param("conversacion") Conversacion conversacion,
            @Param("usuario") Usuario usuario
    );

    @Query("""
            SELECT m FROM Mensaje m 
            WHERE m.emisor = :emisor 
            AND m.fechaEnvio BETWEEN :inicio AND :fin 
            ORDER BY m.fechaEnvio DESC
            """)
    List<Mensaje> findByEmisorAndFechaBetween(
            @Param("emisor") Usuario emisor,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("""
            SELECT m FROM Mensaje m 
            WHERE m.conversacion IN (
                SELECT c FROM Conversacion c 
                WHERE :usuario MEMBER OF c.participantes
            ) 
            AND LOWER(m.contenido) LIKE LOWER(CONCAT('%', :keyword, '%')) 
            ORDER BY m.fechaEnvio DESC
            """)
    Page<Mensaje> searchMessages(
            @Param("usuario") Usuario usuario,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
            SELECT 
                COUNT(m) as totalMensajes,
                COUNT(CASE WHEN m.leido = false THEN 1 END) as noLeidos,
                MIN(m.fechaEnvio) as primerMensaje,
                MAX(m.fechaEnvio) as ultimoMensaje
            FROM Mensaje m 
            WHERE m.conversacion = :conversacion
            """)
    Object[] getMessageStatistics(@Param("conversacion") Conversacion conversacion);

    @Modifying
    @Query("DELETE FROM Mensaje m WHERE m.fechaEnvio < :fecha")
    void deleteOldMessages(@Param("fecha") LocalDateTime fecha);

    @Query("""
            SELECT m FROM Mensaje m 
            WHERE m.conversacion = :conversacion 
            ORDER BY m.fechaEnvio DESC
            """)
    List<Mensaje> findLastMessages(
            @Param("conversacion") Conversacion conversacion,
            Pageable pageable
    );

    @Query("SELECT COUNT(m) FROM Mensaje m WHERE m.conversacion = :conversacion")
    long countByConversacion(@Param("conversacion") Conversacion conversacion);
}