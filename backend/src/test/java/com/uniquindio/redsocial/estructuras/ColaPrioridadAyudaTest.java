
package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.SolicitudAyuda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas para ColaPrioridadAyuda")
class ColaPrioridadAyudaTest {

    private ColaPrioridadAyuda cola;
    private SolicitudAyuda solicitudAlta;
    private SolicitudAyuda solicitudMedia;
    private SolicitudAyuda solicitudBaja;

    @BeforeEach
    void setUp() {
        cola = new ColaPrioridadAyuda();
        solicitudAlta = new SolicitudAyuda(
                "Duda de álgebra",
                "maria@uq.edu.co",
                SolicitudAyuda.Prioridad.ALTA
        );
        solicitudAlta.setId(1L);

        solicitudMedia = new SolicitudAyuda(
                "Problema con grafos",
                "juan@uq.edu.co",
                SolicitudAyuda.Prioridad.MEDIA
        );
        solicitudMedia.setId(2L);

        solicitudBaja = new SolicitudAyuda(
                "Consulta sobre Java",
                "pedro@uq.edu.co",
                SolicitudAyuda.Prioridad.BAJA
        );
        solicitudBaja.setId(3L);
    }

    @Nested
    @DisplayName("Pruebas de operaciones básicas")
    class OperacionesBasicas {

        @Test
        @DisplayName("Debería agregar y atender solicitudes en orden de prioridad")
        void testAgregarYAtenderSolicitudes() {
            cola.agregarSolicitud(solicitudMedia);
            cola.agregarSolicitud(solicitudAlta);
            cola.agregarSolicitud(solicitudBaja);

            assertEquals(solicitudAlta.getId(), cola.atenderSolicitud().getId(), "Primera solicitud debe ser de prioridad ALTA");
            assertEquals(solicitudMedia.getId(), cola.atenderSolicitud().getId(), "Segunda solicitud debe ser de prioridad MEDIA");
            assertEquals(solicitudBaja.getId(), cola.atenderSolicitud().getId(), "Tercera solicitud debe ser de prioridad BAJA");
            assertTrue(cola.estaVacia(), "La cola debe quedar vacía");
        }

        @Test
        @DisplayName("Debería manejar correctamente cola vacía")
        void testColaVacia() {
            assertTrue(cola.estaVacia(), "Cola nueva debe estar vacía");
            assertNull(cola.atenderSolicitud(), "Atender cola vacía debe retornar null");
            assertEquals(0, cola.obtenerTamanio(), "Tamaño debe ser 0");
        }

        @Test
        @DisplayName("No debería permitir agregar solicitudes null")
        void testAgregarSolicitudNull() {
            assertThrows(IllegalArgumentException.class, () -> cola.agregarSolicitud(null),
                    "Debe lanzar excepción al agregar solicitud null");
        }
    }

    @Nested
    @DisplayName("Pruebas de consulta y tamaño")
    class ConsultaYTamanio {

        @Test
        @DisplayName("Debería mantener el orden correcto al consultar solicitudes")
        void testObtenerSolicitudes() {
            cola.agregarSolicitud(solicitudMedia);
            cola.agregarSolicitud(solicitudAlta);
            cola.agregarSolicitud(solicitudBaja);

            List<SolicitudAyuda> solicitudes = cola.obtenerSolicitudes();
            assertEquals(3, solicitudes.size(), "Debe contener todas las solicitudes");
            assertEquals(solicitudAlta.getId(), solicitudes.get(0).getId(), "Primera solicitud debe ser de prioridad ALTA");
        }

        @Test
        @DisplayName("Debería mantener el tamaño correcto")
        void testTamanoCola() {
            assertEquals(0, cola.obtenerTamanio(), "Cola vacía debe tener tamaño 0");
            cola.agregarSolicitud(solicitudAlta);
            assertEquals(1, cola.obtenerTamanio(), "Cola con una solicitud debe tener tamaño 1");
            cola.atenderSolicitud();
            assertEquals(0, cola.obtenerTamanio(), "Cola después de atender debe tener tamaño 0");
        }
    }

    @Nested
    @DisplayName("Pruebas de concurrencia")
    class PruebasConcurrencia {

        @Test
        @DisplayName("Debería manejar múltiples hilos agregando solicitudes")
        void testConcurrencia() throws InterruptedException {
            int numHilos = 10;
            CountDownLatch latch = new CountDownLatch(numHilos);
            ExecutorService executor = Executors.newFixedThreadPool(numHilos);

            for (int i = 0; i < numHilos; i++) {
                final long id = i;
                executor.submit(() -> {
                    try {
                        cola.agregarSolicitud(new SolicitudAyuda(
                                "Solicitud " + id,
                                "user" + id + "@uq.edu.co",
                                SolicitudAyuda.Prioridad.MEDIA
                        ));
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            executor.shutdown();
            assertEquals(numHilos, cola.obtenerTamanio(),
                    "Todas las solicitudes deben ser agregadas correctamente");
        }
    }

    @Nested
    @DisplayName("Pruebas de casos extremos")
    class CasosExtremos {

        @Test
        @DisplayName("Debería manejar solicitudes con la misma prioridad")
        void testSolicitudesMismaPrioridad() {
            SolicitudAyuda s1 = new SolicitudAyuda("S1", "user1@uq.edu.co", SolicitudAyuda.Prioridad.ALTA);
            s1.setId(1L);
            SolicitudAyuda s2 = new SolicitudAyuda("S2", "user2@uq.edu.co", SolicitudAyuda.Prioridad.ALTA);
            s2.setId(2L);

            cola.agregarSolicitud(s1);
            cola.agregarSolicitud(s2);

            assertEquals(2, cola.obtenerTamanio(),
                    "Debe aceptar múltiples solicitudes con la misma prioridad");
        }

        @Test
        @DisplayName("Debería manejar gran cantidad de solicitudes")
        void testMuchasSolicitudes() {
            for (int i = 0; i < 1000; i++) {
                cola.agregarSolicitud(new SolicitudAyuda(
                        "Solicitud " + i,
                        "user" + i + "@uq.edu.co",
                        SolicitudAyuda.Prioridad.MEDIA
                ));
            }

            assertEquals(1000, cola.obtenerTamanio(),
                    "Debe manejar correctamente gran cantidad de solicitudes");
        }
    }
}