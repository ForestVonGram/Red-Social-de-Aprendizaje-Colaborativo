package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Conversacion;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@Tag(name = "Usuarios", description = "API para la gestión de usuarios en el sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Listar todos los usuarios",
            description = "Obtiene la lista de todos los usuarios registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay usuarios registrados")
    })
    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();

            if (usuarios.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No hay usuarios registrados en el sistema");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }

            List<Map<String, Object>> usuariosDTO = usuarios.stream()
                    .map(usuario -> {
                        Map<String, Object> usuarioMap = new HashMap<>();
                        usuarioMap.put("id", usuario.getId());
                        usuarioMap.put("nombre", usuario.getNombre());
                        usuarioMap.put("correo", usuario.getCorreo());
                        usuarioMap.put("intereses", usuario.getIntereses());
                        return usuarioMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(usuariosDTO);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener la lista de usuarios");
        }
    }

    @Operation(summary = "Obtener conversaciones de un usuario",
            description = "Obtiene todas las conversaciones asociadas a un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversaciones obtenidas exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "204", description = "El usuario no tiene conversaciones")
    })
    @GetMapping("/{correo}/conversaciones")
    public ResponseEntity<?> obtenerConversacionesUsuario(
            @Parameter(description = "Correo del usuario", required = true)
            @PathVariable String correo) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.buscarPorCorreo(correo);
            if (usuarioOpt.isEmpty()) {
                throw new IllegalArgumentException("Usuario no encontrado");
            }

            Usuario usuario = usuarioOpt.get();
            List<Conversacion> conversaciones = usuarioService.obtenerConversaciones(usuario.getId());

            if (conversaciones.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "El usuario no tiene conversaciones");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }

            List<Map<String, Object>> conversacionesDetalladas = conversaciones.stream()
                    .map(conv -> {
                        Map<String, Object> convMap = new HashMap<>();
                        convMap.put("id", conv.getId());
                        convMap.put("participantes", conv.getParticipantes().stream()
                                .map(Usuario::getCorreo)
                                .collect(Collectors.toList()));
                        convMap.put("cantidadMensajes", conv.getMensajes().size());
                        return convMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(conversacionesDetalladas);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener las conversaciones del usuario");
        }
    }

    @Operation(summary = "Eliminar usuario",
            description = "Elimina un usuario del sistema por su correo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{correo}")
    public ResponseEntity<?> eliminarUsuario(
            @Parameter(description = "Correo del usuario", required = true)
            @PathVariable String correo) {
        try {
            boolean eliminado = usuarioService.eliminarUsuario(correo);

            if (!eliminado) {
                return ResponseEntity.notFound().build();
            }

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Usuario eliminado exitosamente");
            response.put("correo", correo);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al eliminar el usuario");
        }
    }

    @GetMapping("/{correo}")
    public ResponseEntity<Usuario> obtenerUsuarioPorCorreo(
            @PathVariable String correo) {
        try {
            return usuarioService.buscarPorCorreo(correo)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener el usuario");
        }
    }
}