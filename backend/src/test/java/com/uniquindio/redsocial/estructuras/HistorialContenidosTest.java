package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.model.Valoracion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HistorialContenidosTest {

    private HistorialContenidos historial;
    private Usuario usuario;
    private Contenido contenido;
    private Valoracion valoracion;

    @BeforeEach
    void setUp() {
        historial = new HistorialContenidos();
        usuario = new Usuario("Test User", "test@uq.edu.co", "password", List.of("Java"));
        contenido = new Contenido();
        contenido.setAutor(usuario);
        valoracion = new Valoracion();
        valoracion.setContenido(contenido);
    }

    @Test
    void testAgregarYObtenerElementos() {
        historial.agregar(contenido);
        historial.agregar(valoracion);

        assertEquals(2, historial.getTamanio());
        assertTrue(historial.contiene(contenido));
        assertTrue(historial.contiene(valoracion));
    }

    @Test
    void testObtenerContenidosPorUsuario() {
        historial.agregar(contenido);
        List<Contenido> contenidos = historial.obtenerContenidosPorUsuario(usuario);

        assertFalse(contenidos.isEmpty());
        assertEquals(contenido, contenidos.get(0));
    }

    @Test
    void testObtenerValoracionesPorContenido() {
        historial.agregar(valoracion);
        List<Valoracion> valoraciones = historial.obtenerValoracionesPorContenido(contenido);

        assertFalse(valoraciones.isEmpty());
        assertEquals(valoracion, valoraciones.get(0));
    }

    @Test
    void testLimiteHistorial() {
        for (int i = 0; i < 150; i++) {
            historial.agregar(new Contenido());
        }
        assertTrue(historial.getTamanio() <= 100);
    }

    @Test
    void testObtenerHistorialPorFecha() {
        LocalDateTime desde = LocalDateTime.now().minusHours(1);
        historial.agregar(contenido);
        LocalDateTime hasta = LocalDateTime.now().plusHours(1);

        List<Object> historialFiltrado = historial.obtenerHistorialPorFecha(desde, hasta);
        assertFalse(historialFiltrado.isEmpty());
        assertTrue(historialFiltrado.contains(contenido));
    }

    @Test
    void testLimpiarHistorial() {
        historial.agregar(contenido);
        historial.limpiarHistorial();

        assertTrue(historial.estaVacio());
        assertEquals(0, historial.getTamanio());
    }

    @Test
    void testObtenerUltimoElemento() {
        historial.agregar(contenido);
        historial.agregar(valoracion);

        assertEquals(valoracion, historial.obtenerUltimoElemento().orElse(null));
    }
}