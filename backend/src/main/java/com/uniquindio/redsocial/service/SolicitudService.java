package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.model.Moderador;
import com.uniquindio.redsocial.model.Solicitud;
import com.uniquindio.redsocial.repository.ModeradorRepository;
import com.uniquindio.redsocial.repository.SolicitudRepository;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;

    private final UsuarioRepository usuarioRepository;

    private final ModeradorRepository moderadorRepository;

    private final ColaPrioridadSolicitudes colaPrioridad;

    public Solicitud agregarSolicitud(String estudiante, String descripcion, int prioridad) {
        if (estudiante == null || estudiante.trim().isEmpty()) {
            throw new IllegalArgumentException("El estudiante no puede estar vacío");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }
        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5");
        }

        if (!usuarioRepository.existsByCorreo(estudiante)) {
            throw new IllegalArgumentException("El estudiante no existe en el sistema");
        }

        Solicitud solicitud = new Solicitud(estudiante, descripcion, prioridad);
        solicitud = solicitudRepository.save(solicitud);
        colaPrioridad.agregarSolicitud(solicitud);

        return solicitud;
    }

    public Solicitud atenderSolicitud() {
        Solicitud solicitud = colaPrioridad.atenderSolicitud();
        if (solicitud == null) {
            return null;
        }

        List<Moderador> moderadoresDisponibles = moderadorRepository.findModeradorDisponibleParaSolicitud(5, null);
        if (!moderadoresDisponibles.isEmpty()) {
            Moderador moderador = moderadoresDisponibles.get(0);
            solicitud.setModeradorAsignado(moderador);
        }

        solicitud.setResuelta(true);
        solicitud.setFechaResolucion(LocalDateTime.now());
        return solicitudRepository.save(solicitud);
    }

    public List<Solicitud> getSolicitudesOrdenadas() {
        return solicitudRepository.findByResueltaFalseOrderByPrioridadDescFechaCreacionAsc();
    }

    public List<Solicitud> getSolicitudesPendientes() {
        return solicitudRepository.findByResueltaFalse();
    }

    public List<Solicitud> getSolicitudesEstudiante(String correoEstudiante) {
        return solicitudRepository.findByEstudianteOrderByFechaCreacionDesc(correoEstudiante);
    }

    public Optional<Solicitud> obtenerSolicitud(Long id) {
        return solicitudRepository.findById(id);
    }

    public Solicitud actualizarEstado(Long id, boolean resuelta) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        solicitud.setResuelta(resuelta);
        if (resuelta) {
            solicitud.setFechaResolucion(LocalDateTime.now());
        }

        return solicitudRepository.save(solicitud);
    }

    public void asignarModerador(Long solicitudId, Long moderadorId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        Moderador moderador = moderadorRepository.findById(moderadorId)
                .orElseThrow(() -> new IllegalArgumentException("Moderador no encontrado"));

        solicitud.setModeradorAsignado(moderador);
        solicitudRepository.save(solicitud);
    }

    public List<Solicitud> getSolicitudesModerador(Long moderadorId) {
        return solicitudRepository.findByModeradorAsignadoIdOrderByPrioridadDescFechaCreacionAsc(moderadorId);
    }

    public boolean tieneSolicitudPendiente(String correoEstudiante) {
        return solicitudRepository.existsByEstudianteAndResueltaFalse(correoEstudiante);
    }

    public long contarSolicitudesPendientesModerador(Long moderadorId) {
        return solicitudRepository.countByModeradorAsignadoIdAndResueltaFalse(moderadorId);
    }
}