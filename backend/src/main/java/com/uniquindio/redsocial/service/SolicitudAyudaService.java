package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.ColaPrioridadAyuda;
import com.uniquindio.redsocial.model.SolicitudAyuda;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudAyudaService {
    private final ColaPrioridadAyuda cola = new ColaPrioridadAyuda();

    public void registrarSolicitud(String correo, String descripcion, SolicitudAyuda.Prioridad prioridad) {
        SolicitudAyuda solicitud = new SolicitudAyuda(
                java.util.UUID.randomUUID().toString(),
                descripcion,
                correo,
                prioridad
        );
        cola.agregarSolicitud(solicitud);
    }

    public SolicitudAyuda atenderSiguiente() {
        return cola.atenderSolicitud();
    }

    public List<SolicitudAyuda> listarSolicitudesOrdenadas() {
        return cola.obtenerSolicitudes().stream()
                .sorted()
                .collect(Collectors.toList());
    }
}