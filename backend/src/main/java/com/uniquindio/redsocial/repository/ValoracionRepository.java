package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.model.Valoracion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion, String> {

    @Query("""
            SELECT v FROM Valoracion v 
            WHERE v.contenido = :contenido 
            ORDER BY v.estrellas DESC
            """)
    List<Valoracion> findByContenido(@Param("contenido") Contenido contenido);

    @Query("""
            SELECT v FROM Valoracion v 
            WHERE v.contenido = :contenido 
            ORDER BY v.estrellas DESC
            """)
    Page<Valoracion> findByContenidoPaged(
            @Param("contenido") Contenido contenido,
            Pageable pageable
    );

    @Query("""
            SELECT AVG(v.estrellas) FROM Valoracion v 
            WHERE v.contenido = :contenido
            """)
    Double calcularPromedioValoraciones(@Param("contenido") Contenido contenido);

    @Query("""
            SELECT v.estrellas, COUNT(v) FROM Valoracion v 
            WHERE v.contenido = :contenido 
            GROUP BY v.estrellas 
            ORDER BY v.estrellas DESC
            """)
    List<Object[]> contarValoracionesPorEstrellas(@Param("contenido") Contenido contenido);

    @Query("""
            SELECT v FROM Valoracion v 
            WHERE v.contenido = :contenido 
            AND v.comentario IS NOT NULL 
            AND LENGTH(v.comentario) > 0 
            ORDER BY v.estrellas DESC
            """)
    List<Valoracion> findValoracionesConComentarios(@Param("contenido") Contenido contenido);

    @Query("""
            SELECT v FROM Valoracion v 
            WHERE v.contenido = :contenido 
            AND v.estrellas BETWEEN :minEstrellas AND :maxEstrellas 
            ORDER BY v.estrellas DESC
            """)
    List<Valoracion> findByRangoEstrellas(
            @Param("contenido") Contenido contenido,
            @Param("minEstrellas") int minEstrellas,
            @Param("maxEstrellas") int maxEstrellas
    );

    @Query("""
            SELECT 
                COUNT(v) as total,
                AVG(v.estrellas) as promedio,
                MIN(v.estrellas) as minimo,
                MAX(v.estrellas) as maximo,
                COUNT(CASE WHEN v.comentario IS NOT NULL AND LENGTH(v.comentario) > 0 THEN 1 END) as conComentarios
            FROM Valoracion v 
            WHERE v.contenido = :contenido
            """)
    Object[] getValoracionStats(@Param("contenido") Contenido contenido);

    List<Valoracion> findByContenidoAndEstrellas(Contenido contenido, int estrellas);

    boolean existsByContenidoAndId(Contenido contenido, String id);

    @Query("""
            SELECT v FROM Valoracion v 
            WHERE v.contenido = :contenido 
            AND LOWER(v.comentario) LIKE LOWER(CONCAT('%', :keyword, '%')) 
            ORDER BY v.estrellas DESC
            """)
    List<Valoracion> buscarPorComentario(
            @Param("contenido") Contenido contenido,
            @Param("keyword") String keyword
    );

    @Query("""
            SELECT v FROM Valoracion v 
            WHERE v.contenido = :contenido 
            AND v.estrellas >= :minEstrellas 
            ORDER BY v.estrellas DESC
            """)
    List<Valoracion> findMejoresValoraciones(
            @Param("contenido") Contenido contenido,
            @Param("minEstrellas") int minEstrellas
    );
}