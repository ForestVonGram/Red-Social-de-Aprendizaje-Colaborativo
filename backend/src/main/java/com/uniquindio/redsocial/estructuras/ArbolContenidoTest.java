package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.model.Usuario;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ArbolContenidoTest {

    @Test
    public void testInsertarYBuscarContenido() {
        ArbolContenido arbol = new ArbolContenido();

        Usuario autor = new Usuario("u1", "Laura", "laura@uq.edu.co", "abc123", List.of("IA"));

        Contenido c1 = new Contenido(
                "c1",
                "Introducción a Java",
                "Curso básico de Java",
                "VIDEO",
                "http://video.com/java",
                autor,
                new Date(),
                Collections.emptyList()
        );

        Contenido c2 = new Contenido(
                "c2",
                "Estructuras de Datos",
                "Explicación de listas, colas y pilas",
                "DOCUMENTO",
                "http://docs.com/estructuras",
                autor,
                new Date(),
                Collections.emptyList()
        );

        arbol.insertar(c1);
        arbol.insertar(c2);

        List<Contenido> encontrados = arbol.buscarPorTitulo("java");

        assertEquals(1, encontrados.size());
        assertEquals("Introducción a Java", encontrados.get(0).getTitulo());
    }
}