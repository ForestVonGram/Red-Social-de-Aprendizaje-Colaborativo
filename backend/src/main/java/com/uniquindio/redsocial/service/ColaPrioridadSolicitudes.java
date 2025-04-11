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
        cola.offer(solicitud);
    }

    public Solicitud atenderSolicitud() {
        return cola.poll();
    }
}
