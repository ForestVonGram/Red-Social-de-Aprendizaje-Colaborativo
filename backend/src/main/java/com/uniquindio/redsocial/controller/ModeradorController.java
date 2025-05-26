package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.ModeradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@RequestMapping("/api/moderador")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@Tag(name = "Moderador", description = "API para la gestión de moderación del sistema")
public class ModeradorController {

    private final ModeradorService moderadorService;

    @Operation(summary = "Eliminar usuario",
            description = "Elimina un usuario del sistema por su correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar esta acción"),
            @ApiResponse(responseCode = "400", description = "Correo electrónico inválido")
    })
    @DeleteMapping("/eliminar-usuario/{correo}")
    public ResponseEntity<Map<String, String>> eliminarUsuario(
            @Parameter(description = "Correo electrónico del usuario a eliminar", required = true)
            @PathVariable @NotBlank @Email(message = "Debe proporcionar un correo electrónico válido")
            String correo) {
        try {
            boolean eliminado = moderadorService.eliminarUsuario(correo);
            Map<String, String> response = new HashMap<>();

            if (eliminado) {
                response.put("mensaje", "Usuario eliminado correctamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "No se encontró el usuario especificado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al eliminar el usuario");
        }
    }

    @Operation(summary = "Eliminar contenido",
            description = "Elimina contenido específico del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Contenido no encontrado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar esta acción"),
            @ApiResponse(responseCode = "400", description = "ID de contenido inválido")
    })
    @DeleteMapping("/eliminar-contenido/{id}")
    public ResponseEntity<Map<String, String>> eliminarContenido(
            @Parameter(description = "ID del contenido a eliminar", required = true)
            @PathVariable @NotBlank(message = "El ID no puede estar vacío")
            String id) {
        try {
            boolean eliminado = moderadorService.eliminarContenido(id);
            Map<String, String> response = new HashMap<>();

            if (eliminado) {
                response.put("mensaje", "Contenido eliminado correctamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "No se encontró el contenido especificado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al eliminar el contenido");
        }
    }

    @Operation(summary = "Listar usuarios",
            description = "Obtiene la lista de todos los usuarios del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay usuarios registrados"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar esta acción")
    })
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        try {
            List<Usuario> usuarios = moderadorService.listarUsuarios();

            if (usuarios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(usuarios);
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener la lista de usuarios");
        }
    }

    @Operation(summary = "Listar contenidos",
            description = "Obtiene la lista de todo el contenido del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contenidos recuperada correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay contenidos registrados"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar esta acción")
    })
    @GetMapping("/contenidos")
    public ResponseEntity<List<Contenido>> listarContenidos() {
        try {
            List<Contenido> contenidos = moderadorService.listarContenidos();

            if (contenidos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(contenidos);
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener la lista de contenidos");
        }
    }
}