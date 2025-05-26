
package com.uniquindio.redsocial.estructuras;

import com.uniquindio.redsocial.model.SolicitudAyuda;

import java.util.*;

public class ColaPrioridadAyuda {
    private final PriorityQueue<SolicitudAyuda> cola;

    public ColaPrioridadAyuda() {
        this.cola = new PriorityQueue<>();
    }

    public void agregarSolicitud(SolicitudAyuda solicitud) {
        Objects.requireNonNull(solicitud, "La solicitud no puede ser null");
        cola.offer(solicitud);
    }

    public SolicitudAyuda atenderSolicitud() {
        return cola.poll();
    }

    public SolicitudAyuda verSiguienteSolicitud() {
        return cola.peek();
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }

    public int obtenerTamanio() {
        return cola.size();
    }

    public List<SolicitudAyuda> obtenerSolicitudes() {
        return Collections.unmodifiableList(
                new ArrayList<>(cola)
        );
    }
}