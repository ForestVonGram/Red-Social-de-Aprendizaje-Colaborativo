package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.model.Solicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudService {
    private final ColaPrioridadSolicitudes colaPrioridad;

    public void agregarSolicitud(String estudiante, String descripcion, int prioridad) {
        colaPrioridad.agregarSolicitud(new Solicitud(estudiante, descripcion, prioridad));
    }

    public Solicitud atenderSolicitud() {
        return colaPrioridad.atenderSolicitud();
    }

    public List<String> getSolicitudesOrdenadas() {
        return colaPrioridad.getCola().stream()
                .sorted()
                .map(Solicitud::toString)
                .collect(Collectors.toList());
    }
}
