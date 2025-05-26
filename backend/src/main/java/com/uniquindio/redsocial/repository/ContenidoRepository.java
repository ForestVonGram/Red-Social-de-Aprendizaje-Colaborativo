package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.Contenido;
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
public interface ContenidoRepository extends JpaRepository<Contenido, Long> {

    @Query("""
            SELECT c FROM Contenido c 
            JOIN c.valoraciones v 
            WHERE v.estrellas >= 4 
            GROUP BY c 
            ORDER BY COUNT(v) DESC, AVG(v.estrellas) DESC
            """)
    Page<Contenido> findMostValuedContents(Pageable pageable);

    @Query("""
            SELECT DISTINCT c FROM Contenido c 
            WHERE LOWER(c.titulo) LIKE LOWER(CONCAT('%', :keyword, '%')) 
            OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Contenido> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Page<Contenido> findByTipo(Contenido.TipoContenido tipo, Pageable pageable);

    Page<Contenido> findByAutor(Usuario autor, Pageable pageable);

    @Query("SELECT c FROM Contenido c WHERE c.fechaPublicacion >= :fecha")
    Page<Contenido> findRecentContent(@Param("fecha") LocalDateTime fecha, Pageable pageable);

    @Query("""
            SELECT c FROM Contenido c 
            LEFT JOIN c.valoraciones v 
            WHERE c.tipo = :tipo 
            GROUP BY c 
            ORDER BY AVG(COALESCE(v.estrellas, 0)) DESC
            """)
    Page<Contenido> findMostValuedByType(
            @Param("tipo") Contenido.TipoContenido tipo,
            Pageable pageable
    );

    @Query("SELECT c FROM Contenido c WHERE c.valoraciones IS EMPTY")
    Page<Contenido> findContentWithoutRatings(Pageable pageable);

    @Query("""
            SELECT c FROM Contenido c 
            LEFT JOIN c.valoraciones v 
            GROUP BY c 
            HAVING AVG(v.estrellas) BETWEEN :minRating AND :maxRating
            """)
    Page<Contenido> findByAverageRatingBetween(
            @Param("minRating") Double minRating,
            @Param("maxRating") Double maxRating,
            Pageable pageable
    );

    @Query("""
            SELECT c FROM Contenido c 
            LEFT JOIN c.valoraciones v 
            GROUP BY c 
            HAVING COUNT(v) >= :minValoraciones
            """)
    Page<Contenido> findPopularContent(
            @Param("minValoraciones") Long minValoraciones,
            Pageable pageable
    );

    Optional<Contenido> findByUrl(String url);

    Page<Contenido> findByAutorAndTipo(
            Usuario autor,
            Contenido.TipoContenido tipo,
            Pageable pageable
    );

    @Query("""
            SELECT COUNT(c) as total,
                   c.tipo as tipo,
                   AVG(
                       CASE 
                           WHEN v.estrellas IS NOT NULL 
                           THEN v.estrellas 
                           ELSE 0 
                       END
                   ) as promedioValoracion
            FROM Contenido c
            LEFT JOIN c.valoraciones v
            GROUP BY c.tipo
            """)
    List<Object[]> getContentStatistics();

    boolean existsByTituloAndAutor(String titulo, Usuario autor);

    @Modifying
    @Query("""
            DELETE FROM Contenido c 
            WHERE c.valoraciones IS EMPTY 
            AND c.fechaPublicacion < :fecha
            """)
    void deleteOldUnratedContent(@Param("fecha") LocalDateTime fecha);
}