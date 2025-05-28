package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.model.Usuario;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.util.*;

import static org.junit.Assert.*;

@SpringBootTest
public class ArbolContenidoTest {

    private ArbolContenido arbol;
    private Usuario usuarioPrueba;

    @BeforeEach
    void setUp() {
        arbol = new ArbolContenido();
        usuarioPrueba = new Usuario();
        usuarioPrueba.setId(1L);
        usuarioPrueba.setNombre("Usuario Prueba");
        usuarioPrueba.setCorreo("test@test.com");
    }

    private Contenido crearContenido(Long id, Date fecha, String tipo) {
        Contenido contenido = new Contenido();
        contenido.setId(id);
        contenido.setTitulo("Contenido " + id);
        contenido.setDescripcion("Descripción del contenido " + id);
        contenido.setTipo(Contenido.TipoContenido.valueOf(tipo));
        contenido.setUrl("https://ejemplo.com/contenido/" + id);
        contenido.setAutor(usuarioPrueba);
        contenido.setFechaPublicacion(fecha.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        contenido.setValoraciones(new ArrayList<>());
        return contenido;
    }


    @Test
    public void testInsertarContenido() {
        // Arrange
        Contenido contenido = crearContenido(1L, new Date(), "VIDEO");

        // Act
        arbol.insertarContenido(contenido);

        // Assert
        assertFalse(arbol.estaVacio());
        assertEquals(1, arbol.obtenerTamanio());
        assertTrue(arbol.estaBalanceado());
    }

    @Test
    public void testInsertarMultiplesContenidos() {
        // Arrange
        List<Contenido> contenidos = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        for (long i = 1; i <= 5; i++) {
            cal.add(Calendar.HOUR, 1);
            contenidos.add(crearContenido(i, cal.getTime(), "VIDEO"));
        }

        // Act
        contenidos.forEach(arbol::insertarContenido);

        // Assert
        assertEquals(5, arbol.obtenerTamanio());
        assertTrue(arbol.estaBalanceado());
        assertTrue(arbol.obtenerAlturaArbol() <= 4);
    }

    @Test
    public void testBuscarPorFecha() {
        // Arrange
        Calendar cal = Calendar.getInstance();
        Date fechaBusqueda = cal.getTime();

        // Crear contenidos en la misma fecha
        Contenido contenido1 = crearContenido(1L, fechaBusqueda, "VIDEO");
        cal.add(Calendar.HOUR, 2);
        Contenido contenido2 = crearContenido(2L, cal.getTime(), "IMAGEN");

        arbol.insertarContenido(contenido1);
        arbol.insertarContenido(contenido2);

        // Act
        List<Contenido> resultados = arbol.buscarPorFecha(fechaBusqueda);

        // Assert
        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
        assertEquals(contenido1.getId(), resultados.get(0).getId());
    }

    @Test
    public void testBuscarPorTipo() {
        // Arrange
        Calendar cal = Calendar.getInstance();

        Contenido video1 = crearContenido(1L, cal.getTime(), "VIDEO");
        cal.add(Calendar.HOUR, 1);
        Contenido imagen1 = crearContenido(2L, cal.getTime(), "IMAGEN");
        cal.add(Calendar.HOUR, 1);
        Contenido video2 = crearContenido(3L, cal.getTime(), "VIDEO");

        arbol.insertarContenido(video1);
        arbol.insertarContenido(imagen1);
        arbol.insertarContenido(video2);

        // Act
        List<Contenido> videos = arbol.buscarPorTipo("VIDEO");
        List<Contenido> imagenes = arbol.buscarPorTipo("IMAGEN");

        // Assert
        assertEquals(2, videos.size());
        assertEquals(1, imagenes.size());
        assertTrue(videos.stream().allMatch(c -> false));
        assertTrue(imagenes.stream().allMatch(c -> false));
    }

    @Test
    public void testEliminarContenido() {
        // Arrange
        Calendar cal = Calendar.getInstance();
        Contenido contenido1 = crearContenido(1L, cal.getTime(), "VIDEO");
        cal.add(Calendar.HOUR, 1);
        Contenido contenido2 = crearContenido(2L, cal.getTime(), "IMAGEN");

        arbol.insertarContenido(contenido1);
        arbol.insertarContenido(contenido2);

        // Act
        arbol.eliminarContenido(1L);

        // Assert
        assertEquals(1, arbol.obtenerTamanio());
        assertTrue(arbol.estaBalanceado());

        List<Contenido> contenidos = arbol.obtenerContenidoInOrder();
        assertEquals(1, contenidos.size());
        assertEquals(Long.valueOf(2L), contenidos.get(0).getId());
    }

    @Test
    public void testOrdenamientoPorFecha() {
        // Arrange
        Calendar cal = Calendar.getInstance();
        List<Contenido> contenidosOriginales = new ArrayList<>();

        for (long i = 1; i <= 5; i++) {
            cal.add(Calendar.HOUR, 1);
            contenidosOriginales.add(crearContenido(i, cal.getTime(), "VIDEO"));
        }

        // Insertar en orden aleatorio
        List<Contenido> contenidosDesordenados = new ArrayList<>(contenidosOriginales);
        Collections.shuffle(contenidosDesordenados);
        contenidosDesordenados.forEach(arbol::insertarContenido);

        // Act
        List<Contenido> contenidosOrdenados = arbol.obtenerContenidoInOrder();

        // Assert
        assertEquals(contenidosOriginales.size(), contenidosOrdenados.size());
        for (int i = 0; i < contenidosOrdenados.size() - 1; i++) {
            assertFalse(contenidosOrdenados.get(i).getFechaPublicacion().isAfter(contenidosOrdenados.get(i + 1).getFechaPublicacion()));
        }
    }

    @Test
    public void testExcepcionesInsercion() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> arbol.insertarContenido(null));
    }

    @Test
    public void testExcepcionesBusqueda() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> arbol.buscarPorTipo(null));
        assertThrows(IllegalArgumentException.class, () -> arbol.buscarPorTipo(""));
        assertThrows(IllegalArgumentException.class, () -> arbol.buscarPorTipo("  "));
    }

    @Test
    public void testBalanceoAutomatico() {
        // Arrange
        Calendar cal = Calendar.getInstance();

        // Insertar contenidos en orden secuencial para forzar desbalance
        for (long i = 1; i <= 7; i++) {
            cal.add(Calendar.HOUR, 1);
            arbol.insertarContenido(crearContenido(i, cal.getTime(), "VIDEO"));
        }

        // Assert
        assertTrue(arbol.estaBalanceado());
        assertTrue(arbol.obtenerAlturaArbol() <= 4);
    }

    @Test
    public void testLimpiarArbol() {
        // Arrange
        Calendar cal = Calendar.getInstance();
        for (long i = 1; i <= 3; i++) {
            cal.add(Calendar.HOUR, 1);
            arbol.insertarContenido(crearContenido(i, cal.getTime(), "VIDEO"));
        }

        // Act
        arbol.limpiarArbol();

        // Assert
        assertTrue(arbol.estaVacio());
        assertEquals(0, arbol.obtenerTamanio());
    }

    @Test
    public void testBusquedaFechaInexistente() {
        // Arrange
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        arbol.insertarContenido(crearContenido(1L, cal.getTime(), "VIDEO"));

        // Act
        cal.add(Calendar.DAY_OF_YEAR, 2);
        List<Contenido> resultados = arbol.buscarPorFecha(cal.getTime());

        // Assert
        assertTrue(resultados.isEmpty());
    }
}