package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.dto.MensajeDTO;
import com.uniquindio.redsocial.dto.MensajeResponseDTO;
import com.uniquindio.redsocial.model.Mensaje;
import com.uniquindio.redsocial.service.MensajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@Tag(name = "Mensajes", description = "API para la gestión de mensajes entre usuarios")
public class MensajeController {

    private final MensajeService mensajeService;

    @Operation(summary = "Enviar mensaje",
            description = "Envía un nuevo mensaje en una conversación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mensaje enviado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del mensaje inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario o conversación no encontrados"),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado para esta conversación")
    })
    @PostMapping
    public ResponseEntity<MensajeResponseDTO> enviarMensaje(
            @Parameter(description = "Datos del mensaje a enviar", required = true)
            @Valid @RequestBody MensajeDTO dto) {
        try {
            validarMensajeDTO(dto);

            Mensaje mensaje = mensajeService.enviarMensaje(
                    dto.getRemitenteId(),
                    dto.getConversacionId(),
                    dto.getContenido()
            );

            MensajeResponseDTO responseDTO = MensajeResponseDTO.fromEntity(mensaje);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Obtener mensajes de una conversación",
            description = "Recupera todos los mensajes de una conversación específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensajes recuperados exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay mensajes en la conversación"),
            @ApiResponse(responseCode = "404", description = "Conversación no encontrada"),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado para ver esta conversación")
    })
    @GetMapping("/conversacion/{id}")
    public ResponseEntity<List<MensajeResponseDTO>> obtenerMensajes(
            @Parameter(description = "ID de la conversación", required = true)
            @PathVariable Long id) {
        try {
            List<Mensaje> mensajes = mensajeService.obtenerMensajesPorConversacion(id);

            if (mensajes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<MensajeResponseDTO> responseDTOs = mensajes.stream()
                    .map(MensajeResponseDTO::fromEntity)
                    .toList();

            return ResponseEntity.ok(responseDTOs);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener los mensajes");
        }
    }

    @Operation(summary = "Eliminar mensaje",
            description = "Elimina un mensaje específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mensaje eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Mensaje no encontrado"),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado para eliminar este mensaje")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMensaje(
            @Parameter(description = "ID del mensaje", required = true)
            @PathVariable @NotBlank(message = "El ID no puede estar vacío")
            String id) {
        try {
            mensajeService.eliminarMensaje(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al eliminar el mensaje");
        }
    }

    private void validarMensajeDTO(MensajeDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El mensaje no puede ser null");
        }
        if (dto.getRemitenteId() == null || dto.getRemitenteId() <= 0) {
            throw new IllegalArgumentException("ID de remitente inválido");
        }
        if (dto.getConversacionId() == null || dto.getConversacionId() <= 0) {
            throw new IllegalArgumentException("ID de conversación inválido");
        }
        if (dto.getContenido() == null || dto.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del mensaje no puede estar vacío");
        }
    }
}
