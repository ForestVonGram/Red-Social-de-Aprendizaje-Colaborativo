package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.dto.SolicitudAyudaDTO;
import com.uniquindio.redsocial.model.SolicitudAyuda;
import com.uniquindio.redsocial.service.SolicitudAyudaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/ayuda")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@Tag(name = "Solicitudes de Ayuda", description = "API para la gestión de solicitudes de ayuda en el sistema")
public class SolicitudAyudaController {

    private final SolicitudAyudaService ayudaService;

    @Operation(summary = "Registrar nueva solicitud de ayuda",
            description = "Registra una nueva solicitud de ayuda en el sistema con la prioridad especificada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la solicitud inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/registrar")
    public ResponseEntity<Map<String, String>> registrarSolicitud(
            @Parameter(description = "Datos de la solicitud de ayuda", required = true)
            @Valid @RequestBody SolicitudAyudaDTO dto) {
        try {
            validarSolicitudDTO(dto);

            ayudaService.registrarSolicitud(
                    dto.getSolicitanteId().toString(),
                    dto.getDescripcion(),
                    dto.getPrioridad()
            );

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Solicitud de ayuda registrada exitosamente");
            response.put("correo", dto.getSolicitanteId().toString());
            response.put("prioridad", dto.getPrioridad().toString());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("no encontrado")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al registrar la solicitud de ayuda");
        }
    }

    @Operation(summary = "Atender siguiente solicitud",
            description = "Obtiene y marca como atendida la siguiente solicitud de ayuda según prioridad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay solicitudes pendientes")
    })
    @GetMapping("/atender")
    public ResponseEntity<?> atenderSolicitud() {
        try {
            SolicitudAyuda solicitud = ayudaService.atenderSiguiente();

            if (solicitud == null) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No hay solicitudes pendientes para atender");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("id", solicitud.getId());
            response.put("correoEstudiante", solicitud.getCorreoEstudiante());
            response.put("descripcion", solicitud.getDescripcion());
            response.put("prioridad", solicitud.getPrioridad());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al atender la solicitud");
        }
    }

    @Operation(summary = "Listar solicitudes pendientes",
            description = "Obtiene la lista de todas las solicitudes de ayuda pendientes, ordenadas por prioridad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay solicitudes pendientes")
    })
    @GetMapping("/listar")
    public ResponseEntity<?> listarSolicitudes() {
        try {
            List<SolicitudAyuda> solicitudes = ayudaService.listarSolicitudesOrdenadas();

            if (solicitudes.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No hay solicitudes de ayuda registradas");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }

            List<Map<String, Object>> solicitudesDetalladas = solicitudes.stream()
                    .map(solicitud -> {
                        Map<String, Object> solicitudMap = new HashMap<>();
                        solicitudMap.put("id", solicitud.getId());
                        solicitudMap.put("correoEstudiante", solicitud.getCorreoEstudiante());
                        solicitudMap.put("descripcion", solicitud.getDescripcion());
                        solicitudMap.put("prioridad", solicitud.getPrioridad());
                        return solicitudMap;
                    })
                    .toList();

            return ResponseEntity.ok(solicitudesDetalladas);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al listar las solicitudes");
        }
    }

    private void validarSolicitudDTO(SolicitudAyudaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos de la solicitud no pueden ser nulos");
        }

        if (dto.getSolicitanteId().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }

        if (!dto.getSolicitanteId().toString().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El correo electrónico no es válido");
        }

        if (dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }

        if (dto.getDescripcion().length() < 10) {
            throw new IllegalArgumentException("La descripción debe tener al menos 10 caracteres");
        }

        if (dto.getPrioridad() == null) {
            throw new IllegalArgumentException("La prioridad es obligatoria");
        }
    }
}