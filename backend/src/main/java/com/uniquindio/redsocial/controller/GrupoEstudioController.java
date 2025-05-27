package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.GrupoEstudio;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.GrupoEstudioService;
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

@RestController
@RequestMapping("/api/grupos/automaticos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@Tag(name = "Grupos de Estudio", description = "API para gestionar grupos de estudio automáticos")
public class GrupoEstudioController {

    private final GrupoEstudioService grupoEstudioService;

    @Operation(summary = "Formar grupos de estudio",
            description = "Forma grupos de estudio automáticamente basados en intereses comunes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupos formados exitosamente"),
            @ApiResponse(responseCode = "204", description = "No se pudieron formar grupos"),
            @ApiResponse(responseCode = "500", description = "Error interno al formar grupos")
    })
    @GetMapping("/formar")
    public ResponseEntity<List<GrupoEstudio>> formarGrupos() {
        try {
            List<GrupoEstudio> grupos = grupoEstudioService.formarGruposPorIntereses();
            if (grupos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(grupos);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al formar los grupos de estudio", e);
        }
    }

    @Operation(summary = "Registrar usuario para grupos de estudio",
            description = "Registra un nuevo usuario para participar en grupos de estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos"),
            @ApiResponse(responseCode = "409", description = "Usuario ya registrado")
    })
    @PostMapping("/usuarios")
    public ResponseEntity<Void> registrarUsuario(
            @Parameter(description = "Datos del usuario a registrar", required = true)
            @Valid @RequestBody Usuario usuario) {
        try {
            grupoEstudioService.registrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al registrar el usuario", e);
        }
    }

    @Operation(summary = "Conectar usuarios",
            description = "Establece una conexión entre dos usuarios para grupos de estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuarios conectados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de conexión inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario(s) no encontrado(s)")
    })
    @PostMapping("/conexiones")
    public ResponseEntity<Void> conectarUsuarios(
            @Parameter(description = "Correo del primer usuario", required = true)
            @RequestParam String correo1,
            @Parameter(description = "Correo del segundo usuario", required = true)
            @RequestParam String correo2) {
        try {
            grupoEstudioService.conectarUsuarios(correo1, correo2);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al conectar usuarios", e);
        }
    }

    @Operation(summary = "Obtener todos los usuarios",
            description = "Obtiene la lista de todos los usuarios registrados para grupos de estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay usuarios registrados"),
            @ApiResponse(responseCode = "500", description = "Error interno al obtener usuarios")
    })
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        try {
            List<Usuario> usuarios = grupoEstudioService.obtenerTodosLosUsuarios();
            if (usuarios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener los usuarios", e);
        }
    }

    @Operation(summary = "Obtener usuario por correo",
            description = "Obtiene un usuario específico por su correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Correo inválido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno al buscar usuario")
    })
    @GetMapping("/usuarios/{correo}")
    public ResponseEntity<Usuario> obtenerUsuario(
            @Parameter(description = "Correo del usuario a buscar", required = true)
            @PathVariable String correo) {
        try {
            Usuario usuario = grupoEstudioService.obtenerUsuario(correo);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener el usuario", e);
        }
    }

    @Operation(summary = "Eliminar usuario",
            description = "Elimina un usuario del sistema de grupos de estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Correo inválido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno al eliminar usuario")
    })
    @DeleteMapping("/usuarios/{correo}")
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "Correo del usuario a eliminar", required = true)
            @PathVariable String correo) {
        try {
            grupoEstudioService.eliminarUsuario(correo);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al eliminar el usuario", e);
        }
    }
}
