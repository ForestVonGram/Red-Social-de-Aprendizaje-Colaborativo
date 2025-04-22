package com.uniquindio.redsocial.repository;

import com.uniquindio.redsocial.model.Contenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContenidoRepository extends JpaRepository<Contenido, String> {

    @Query("SELECT c FROM Contenido c JOIN c.valoraciones v WHERE v.estrellas >= 4 GROUP BY c ORDER BY COUNT(v) DESC")
    List<Contenido> findMostValuedContents();
}