package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.SolicitudAyuda;

import java.util.PriorityQueue;

public class ColaPrioridadAyuda {
    private final PriorityQueue<SolicitudAyuda> cola = new PriorityQueue<>();

    public void agregarSolicitud(SolicitudAyuda solicitud) {
        cola.offer(solicitud);
    }

    public SolicitudAyuda atenderSolicitud() {
        return cola.poll();
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }

    public PriorityQueue<SolicitudAyuda> obtenerSolicitudes() {
        return new PriorityQueue<>(cola);
    }
}
