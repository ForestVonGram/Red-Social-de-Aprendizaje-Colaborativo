package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.model.Solicitud;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.PriorityQueue;

@Service
@Getter
public class ColaPrioridadSolicitudes {
    private final PriorityQueue<Solicitud> cola = new PriorityQueue<>();

    public void agregarSolicitud(Solicitud solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        cola.offer(solicitud);
    }

    public Solicitud atenderSolicitud() {
        if (cola.isEmpty()) {
            return null;
        }
        return cola.poll();
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }

    public int getTamanio() {
        return cola.size();
    }
}