package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.SolicitudAyuda;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ColaPrioridadAyudaTest {

    @Test
    public void testAgregarYAtenderSolicitudes() {
        ColaPrioridadAyuda cola = new ColaPrioridadAyuda();

        SolicitudAyuda s1 = new SolicitudAyuda("1", "Problema con grafos", "juan@uq.edu.co", SolicitudAyuda.Prioridad.MEDIA);
        SolicitudAyuda s2 = new SolicitudAyuda("2", "Duda de álgebra", "maria@uq.edu.co", SolicitudAyuda.Prioridad.ALTA);
        SolicitudAyuda s3 = new SolicitudAyuda("3", "Consulta sobre Java", "pedro@uq.edu.co", SolicitudAyuda.Prioridad.BAJA);

        cola.agregarSolicitud(s1);
        cola.agregarSolicitud(s2);
        cola.agregarSolicitud(s3);

        SolicitudAyuda atendida1 = cola.atenderSolicitud();
        SolicitudAyuda atendida2 = cola.atenderSolicitud();
        SolicitudAyuda atendida3 = cola.atenderSolicitud();

        assertEquals("2", atendida1.getId()); // Prioridad ALTA
        assertEquals("1", atendida2.getId()); // Prioridad MEDIA
        assertEquals("3", atendida3.getId()); // Prioridad BAJA
        assertTrue(cola.estaVacia());
    }
}