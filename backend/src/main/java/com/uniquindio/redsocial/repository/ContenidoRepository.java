package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.Contenido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContenidoRepository extends JpaRepository<Contenido, Long> {
    @Query(value = "SELECT c FROM Contenido c JOIN c.valoraciones v " +
            "WHERE v.estrellas >= 4 GROUP BY c " +
            "ORDER BY COUNT(v) DESC")
    Page<Contenido> findMostValuedContents(Pageable pageable);
}