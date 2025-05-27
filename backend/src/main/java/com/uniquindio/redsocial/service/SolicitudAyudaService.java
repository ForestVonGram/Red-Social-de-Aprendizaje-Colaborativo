package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.estructuras.ColaPrioridadAyuda;
import com.uniquindio.redsocial.model.SolicitudAyuda;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.model.Moderador;
import com.uniquindio.redsocial.repository.SolicitudAyudaRepository;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import com.uniquindio.redsocial.repository.ModeradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SolicitudAyudaService {

    private final SolicitudAyudaRepository solicitudAyudaRepository;

    private final UsuarioRepository usuarioRepository;

    private final ModeradorRepository moderadorRepository;

    private final ColaPrioridadAyuda colaPrioridad = new ColaPrioridadAyuda();

    public SolicitudAyuda registrarSolicitud(String correo, String descripcion, SolicitudAyuda.Prioridad prioridad) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
        if (usuario.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado con el correo: " + correo);
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad no puede ser nula");
        }

        SolicitudAyuda solicitud = new SolicitudAyuda(descripcion, correo, prioridad);
        solicitud = solicitudAyudaRepository.save(solicitud);

        colaPrioridad.agregarSolicitud(solicitud);

        return solicitud;
    }

    public SolicitudAyuda atenderSiguiente() {
        SolicitudAyuda solicitud = colaPrioridad.atenderSolicitud();
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
        return solicitudAyudaRepository.save(solicitud);
    }

    public List<SolicitudAyuda> listarSolicitudesOrdenadas() {
        return colaPrioridad.obtenerSolicitudes().stream()
                .sorted()
                .toList();
    }

    public List<SolicitudAyuda> obtenerSolicitudesPendientes() {
        return solicitudAyudaRepository.findByResueltaFalseOrderByPrioridadAscFechaCreacionAsc();
    }

    public List<SolicitudAyuda> obtenerSolicitudesUsuario(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }
        return solicitudAyudaRepository.findByCorreoEstudianteOrderByFechaCreacionDesc(correo);
    }

    public Optional<SolicitudAyuda> obtenerSolicitud(Long id) {
        return solicitudAyudaRepository.findById(id);
    }

    public SolicitudAyuda actualizarEstado(Long id, boolean resuelta) {
        SolicitudAyuda solicitud = solicitudAyudaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        solicitud.setResuelta(resuelta);
        if (resuelta) {
            solicitud.setFechaResolucion(LocalDateTime.now());
        }

        return solicitudAyudaRepository.save(solicitud);
    }

    public void asignarModerador(Long solicitudId, Long moderadorId) {
        SolicitudAyuda solicitud = solicitudAyudaRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        Moderador moderador = moderadorRepository.findById(moderadorId)
                .orElseThrow(() -> new IllegalArgumentException("Moderador no encontrado"));

        solicitud.setModeradorAsignado(moderador);
        solicitudAyudaRepository.save(solicitud);
    }

    public List<SolicitudAyuda> obtenerSolicitudesModerador(Long moderadorId) {
        return solicitudAyudaRepository.findByModeradorAsignadoIdOrderByPrioridadAscFechaCreacionAsc(moderadorId);
    }
}