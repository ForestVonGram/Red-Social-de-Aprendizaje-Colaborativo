package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.service.ContenidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/contenido")
@CrossOrigin(origins = "*")
@Validated
@Tag(name = "Contenido", description = "API para gestionar el contenido de la plataforma")
public class ContenidoController {

    private final ContenidoService contenidoService;

    @Autowired
    public ContenidoController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }

    @Operation(summary = "Publicar nuevo contenido",
            description = "Permite a un usuario publicar nuevo contenido en la plataforma")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contenido creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de contenido inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/publicar")
    public ResponseEntity<Contenido> publicar(
            @Parameter(description = "ID del usuario que publica", required = true)
            @RequestParam @NotNull(message = "El ID del usuario es requerido") Long idUsuario,

            @Parameter(description = "Título del contenido", required = true)
            @RequestParam @NotBlank(message = "El título es requerido") String titulo,

            @Parameter(description = "Descripción del contenido", required = true)
            @RequestParam @NotBlank(message = "La descripción es requerida") String descripcion,

            @Parameter(description = "Tipo de contenido", required = true)
            @RequestParam @NotBlank(message = "El tipo es requerido") String tipo,

            @Parameter(description = "URL del contenido", required = true)
            @RequestParam @NotBlank(message = "La URL es requerida") String url) {

        try {
            Contenido nuevoContenido = contenidoService.publicar(idUsuario, titulo, descripcion, tipo, url);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoContenido);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al publicar el contenido", e);
        }
    }

    @Operation(summary = "Obtener contenidos más valorados",
            description = "Retorna una lista de los contenidos más valorados en la plataforma")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contenidos obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay contenidos valorados")
    })
    @GetMapping("/mas-valorados")
    public ResponseEntity<List<Contenido>> obtenerContenidosMasValorados() {
        try {
            List<Contenido> contenidos = contenidoService.obtenerContenidosMasValorados();

            if (contenidos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(contenidos);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener los contenidos más valorados", e);
        }
    }

    @Operation(summary = "Eliminar contenido",
            description = "Permite eliminar un contenido específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contenido eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Contenido no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarContenido(
            @Parameter(description = "ID del contenido a eliminar", required = true)
            @PathVariable @NotNull Long id) {
        try {
            contenidoService.eliminarContenido(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Actualizar contenido",
            description = "Permite actualizar un contenido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Contenido no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Contenido> actualizarContenido(
            @Parameter(description = "ID del contenido a actualizar", required = true)
            @PathVariable @NotNull Long id,
            @Parameter(description = "Título del contenido", required = true)
            @RequestParam @NotBlank String titulo,
            @Parameter(description = "Descripción del contenido", required = true)
            @RequestParam @NotBlank String descripcion) {
        try {
            Contenido contenidoActualizado = contenidoService.actualizarContenido(id, titulo, descripcion);
            return ResponseEntity.ok(contenidoActualizado);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}