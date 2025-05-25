package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Conversacion;
import com.uniquindio.redsocial.service.ConversacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/conversaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@Tag(name = "Conversaciones", description = "API para gestionar las conversaciones entre usuarios")
public class ConversacionController {

    private final ConversacionService conversacionService;

    @Operation(summary = "Crear una nueva conversación",
            description = "Crea una nueva conversación entre los usuarios especificados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conversación creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Lista de usuarios inválida"),
            @ApiResponse(responseCode = "404", description = "Uno o más usuarios no encontrados")
    })
    @PostMapping
    public ResponseEntity<Conversacion> crearConversacion(
            @Parameter(description = "Lista de IDs de los usuarios participantes", required = true)
            @RequestBody @NotEmpty(message = "La lista de usuarios no puede estar vacía") List<Long> idsUsuarios) {
        try {
            Conversacion nuevaConversacion = conversacionService.crearConversacion(idsUsuarios);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaConversacion);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al crear la conversación", e);
        }
    }

    @Operation(summary = "Obtener una conversación",
            description = "Obtiene los detalles de una conversación específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversación encontrada"),
            @ApiResponse(responseCode = "404", description = "Conversación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Conversacion> obtenerConversacion(
            @Parameter(description = "ID de la conversación", required = true)
            @PathVariable @NotNull(message = "El ID de la conversación es requerido") Long id) {
        try {
            return conversacionService.obtenerConversacion(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Conversación no encontrada"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener la conversación", e);
        }
    }

    @Operation(summary = "Obtener conversaciones de un usuario",
            description = "Obtiene todas las conversaciones en las que participa un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversaciones encontradas"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Conversacion>> obtenerConversacionesPorUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable @NotNull(message = "El ID del usuario es requerido") Long idUsuario) {
        try {
            List<Conversacion> conversaciones = conversacionService.obtenerConversacionesPorUsuario(idUsuario);
            if (conversaciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(conversaciones);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener las conversaciones", e);
        }
    }

    @Operation(summary = "Eliminar una conversación",
            description = "Elimina una conversación específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conversación eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Conversación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarConversacion(
            @Parameter(description = "ID de la conversación", required = true)
            @PathVariable @NotNull(message = "El ID de la conversación es requerido") Long id) {
        try {
            if (conversacionService.eliminarConversacion(id)) {
                return ResponseEntity.noContent().build();
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversación no encontrada");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al eliminar la conversación", e);
        }
    }
}