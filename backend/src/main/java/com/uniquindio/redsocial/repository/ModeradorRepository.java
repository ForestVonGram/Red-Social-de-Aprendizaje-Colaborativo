package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.Moderador;
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
public interface ModeradorRepository extends JpaRepository<Moderador, Long> {

    Optional<Moderador> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    @Query("SELECT m FROM Moderador m WHERE m.activo = true")
    List<Moderador> findAllActivos();

    @Query("""
            SELECT m FROM Moderador m\s
            WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :criterio, '%'))\s
            OR LOWER(m.correo) LIKE LOWER(CONCAT('%', :criterio, '%'))
           \s""")
    Page<Moderador> buscarPorCriterio(@Param("criterio") String criterio, Pageable pageable);

    @Query("""
            SELECT m FROM Moderador m\s
            WHERE m.activo = true\s
            AND SIZE(m.solicitudesAsignadas) < :maxSolicitudes
            ORDER BY SIZE(m.solicitudesAsignadas) ASC
           \s""")
    List<Moderador> findModeradorDisponibleParaSolicitud(
            @Param("maxSolicitudes") int maxSolicitudes,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE Moderador m SET m.ultimaActividad = :fecha WHERE m.id = :id")
    void actualizarUltimaActividad(@Param("id") Long id, @Param("fecha") LocalDateTime fecha);

    @Modifying
    @Query("UPDATE Moderador m SET m.intentosFallidos = m.intentosFallidos + 1 WHERE m.correo = :correo")
    void incrementarIntentosFallidos(@Param("correo") String correo);

    @Modifying
    @Query("UPDATE Moderador m SET m.intentosFallidos = 0 WHERE m.correo = :correo")
    void resetearIntentosFallidos(@Param("correo") String correo);

    @Query("SELECT COUNT(m) FROM Moderador m WHERE m.activo = true")
    long countModeradoresActivos();

    @Modifying
    @Query("UPDATE Moderador m SET m.activo = :estado WHERE m.id = :id")
    void actualizarEstado(@Param("id") Long id, @Param("estado") boolean estado);
}