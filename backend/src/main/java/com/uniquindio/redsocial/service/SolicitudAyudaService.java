package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.ColaPrioridadAyuda;
import com.uniquindio.redsocial.model.SolicitudAyuda;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SolicitudAyudaService {
    private final ColaPrioridadAyuda cola = new ColaPrioridadAyuda();

    public void registrarSolicitud(String correo, String descripcion, SolicitudAyuda.Prioridad prioridad) {
        SolicitudAyuda solicitud = new SolicitudAyuda(UUID.randomUUID().toString(), descripcion, correo, prioridad);
        cola.agregarSolicitud(solicitud);
    }

    public SolicitudAyuda atenderSiguiente() {
        return cola.atenderSolicitud();
    }

    public List<SolicitudAyuda> listarSolicitudes() {
        return cola.obtenerSolicitudes().stream().toList();
    }
}
