package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Solicitud;
import com.uniquindio.redsocial.service.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@Tag(name = "Solicitudes", description = "API para la gestión de solicitudes generales en el sistema")
public class SolicitudController {

    private final SolicitudService solicitudService;

    @Operation(summary = "Agregar nueva solicitud",
            description = "Registra una nueva solicitud en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud agregada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la solicitud inválidos"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @PostMapping("/agregar")
    public ResponseEntity<Map<String, Object>> agregarSolicitud(
            @Parameter(description = "Correo del estudiante", required = true)
            @RequestParam String estudiante,

            @Parameter(description = "Descripción de la solicitud", required = true)
            @RequestParam String descripcion,

            @Parameter(description = "Nivel de prioridad (1-5)", required = true)
            @RequestParam @Min(1) @Max(5) int prioridad) {
        try {
            validarDatosSolicitud(estudiante, descripcion, prioridad);

            solicitudService.agregarSolicitud(estudiante, descripcion, prioridad);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Solicitud agregada exitosamente");
            response.put("estudiante", estudiante);
            response.put("prioridad", prioridad);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("no encontrado")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al agregar la solicitud");
        }
    }

    @Operation(summary = "Atender siguiente solicitud",
            description = "Obtiene y marca como atendida la siguiente solicitud según prioridad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "No hay solicitudes pendientes")
    })
    @GetMapping("/atender")
    public ResponseEntity<?> atenderSolicitud() {
        try {
            Solicitud solicitud = solicitudService.atenderSolicitud();

            if (solicitud == null) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No hay solicitudes pendientes para atender");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("estudiante", solicitud.getEstudiante());
            response.put("descripcion", solicitud.getDescripcion());
            response.put("prioridad", solicitud.getPrioridad());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al atender la solicitud");
        }
    }

    @Operation(summary = "Listar solicitudes pendientes",
            description = "Obtiene la lista de todas las solicitudes pendientes, ordenadas por prioridad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay solicitudes pendientes")
    })
    @GetMapping("/listar")
    public ResponseEntity<?> listarSolicitudes() {
        try {
            List<String> solicitudes = solicitudService.getSolicitudesOrdenadas();

            if (solicitudes.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No hay solicitudes registradas");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }

            return ResponseEntity.ok(solicitudes);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al listar las solicitudes");
        }
    }

    private void validarDatosSolicitud(String estudiante, String descripcion, int prioridad) {
        if (estudiante == null || estudiante.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo del estudiante es obligatorio");
        }

        if (!estudiante.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El correo electrónico no es válido");
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }

        if (descripcion.length() < 10) {
            throw new IllegalArgumentException("La descripción debe tener al menos 10 caracteres");
        }

        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5");
        }
    }
}