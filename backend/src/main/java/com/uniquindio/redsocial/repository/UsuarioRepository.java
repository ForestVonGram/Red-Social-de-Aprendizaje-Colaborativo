package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.Rol;
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
import java.util.Set;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    boolean existsByCorreo(String correo);

    @Query("""
            SELECT u FROM Usuario u\s
            WHERE u.activo = true\s
            AND u.rol = :rol\s
            ORDER BY u.ultimaActividad DESC
           \s""")
    Page<Usuario> findActiveByRol(@Param("rol") Rol rol, Pageable pageable);

    @Query("""
            SELECT DISTINCT u FROM Usuario u\s
            JOIN u.intereses i\s
            WHERE i IN :intereses\s
            AND u.activo = true\s
            AND u.id != :userId
           \s""")
    List<Usuario> findByIntereses(
            @Param("intereses") List<String> intereses,
            @Param("userId") Long userId
    );

    @Modifying
    @Query("""
            UPDATE Usuario u 
            SET u.intentosFallidos = u.intentosFallidos + 1 
            WHERE u.correo = :correo
            """)
    void incrementarIntentosFallidos(@Param("correo") String correo);

    @Modifying
    @Query("UPDATE Usuario u SET u.intentosFallidos = 0 WHERE u.correo = :correo")
    void resetearIntentosFallidos(@Param("correo") String correo);

    @Modifying
    @Query("UPDATE Usuario u SET u.activo = :estado WHERE u.id = :id")
    void actualizarEstado(@Param("id") Long id, @Param("estado") boolean estado);

    @Modifying
    @Query("UPDATE Usuario u SET u.ultimaActividad = :fecha WHERE u.id = :id")
    void actualizarUltimaActividad(
            @Param("id") Long id,
            @Param("fecha") LocalDateTime fecha
    );

    @Query("""
            SELECT u FROM Usuario u\s
            WHERE u.ultimaActividad < :fecha\s
            AND u.activo = true
           \s""")
    List<Usuario> findInactiveUsers(@Param("fecha") LocalDateTime fecha);

    @Query("""
            SELECT DISTINCT u FROM Usuario u\s
            LEFT JOIN u.intereses i\s
            WHERE (:nombre IS NULL OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))\s
            AND (:correo IS NULL OR LOWER(u.correo) LIKE LOWER(CONCAT('%', :correo, '%')))\s
            AND (:rol IS NULL OR u.rol = :rol)\s
            AND (:interes IS NULL OR :interes MEMBER OF u.intereses)\s
            AND u.activo = true
           \s""")
    Page<Usuario> busquedaAvanzada(
            @Param("nombre") String nombre,
            @Param("correo") String correo,
            @Param("rol") Rol rol,
            @Param("interes") String interes,
            Pageable pageable
    );

    @Query("""
            SELECT 
                COUNT(u) as total,
                COUNT(CASE WHEN u.activo = true THEN 1 END) as activos,
                COUNT(CASE WHEN u.rol = 'MODERADOR' THEN 1 END) as moderadores,
                AVG(SIZE(u.contenidos)) as promedioContenidos,
                MAX(u.ultimaActividad) as ultimaActividad
            FROM Usuario u
            """)
    Object[] getUsuarioStats();

    @Query("""
            SELECT u, COUNT(c) as contenidoCount\s
            FROM Usuario u\s
            LEFT JOIN u.contenidos c\s
            WHERE u.activo = true\s
            GROUP BY u\s
            ORDER BY contenidoCount DESC
           \s""")
    Page<Object[]> findMostActiveUsers(Pageable pageable);

    @Query("""
            SELECT DISTINCT u FROM Usuario u\s
            JOIN u.gruposEstudio g\s
            WHERE g.id = :grupoId\s
            AND u.activo = true
           \s""")
    List<Usuario> findByGrupoEstudio(@Param("grupoId") Long grupoId);

    @Query("SELECT DISTINCT i FROM Usuario u JOIN u.intereses i")
    Set<String> findAllUniqueInterests();

    Page<Usuario> findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(
            String nombre,
            String correo,
            Pageable pageable
    );
}