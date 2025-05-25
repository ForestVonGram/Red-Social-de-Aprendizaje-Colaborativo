package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.GrupoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@Tag(name = "Grupos", description = "API para gestionar los grupos de usuarios")
public class GrupoController {

    private final GrupoService grupoService;

    @Operation(summary = "Agregar usuario a un grupo",
            description = "Agrega un nuevo usuario al grupo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario agregado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos"),
            @ApiResponse(responseCode = "409", description = "Usuario ya existe en el grupo")
    })
    @PostMapping("/agregar")
    public ResponseEntity<String> agregarUsuario(
            @Parameter(description = "Datos del usuario a agregar", required = true)
            @Valid @RequestBody Usuario usuario) {
        try {
            grupoService.asignarUsuarioAGrupo(usuario);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Usuario agregado exitosamente al grupo");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al agregar usuario al grupo", e);
        }
    }

    @Operation(summary = "Eliminar usuario del grupo",
            description = "Elimina un usuario del grupo usando su correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado en el grupo")
    })
    @DeleteMapping("/eliminar/{correo}")
    public ResponseEntity<String> eliminarUsuario(
            @Parameter(description = "Correo electrónico del usuario", required = true)
            @PathVariable @NotBlank @Email(message = "Debe proporcionar un correo electrónico válido")
            String correo) {
        try {
            if (grupoService.eliminarUsuarioDelGrupo(correo)) {
                return ResponseEntity.noContent().build();
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Usuario no encontrado en el grupo");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al eliminar usuario del grupo", e);
        }
    }

    @Operation(summary = "Listar usuarios del grupo",
            description = "Obtiene la lista de todos los usuarios en el grupo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay usuarios en el grupo")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarGrupo() {
        try {
            List<Usuario> usuarios = grupoService.listarGrupo();
            if (usuarios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al listar usuarios del grupo", e);
        }
    }

    @Operation(summary = "Buscar usuario en el grupo",
            description = "Busca un usuario en el grupo por su correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/buscar/{correo}")
    public ResponseEntity<Usuario> buscarUsuario(
            @Parameter(description = "Correo electrónico del usuario", required = true)
            @PathVariable @NotBlank @Email(message = "Debe proporcionar un correo electrónico válido")
            String correo) {
        try {
            return grupoService.buscarUsuario(correo)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Usuario no encontrado en el grupo"));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al buscar usuario en el grupo", e);
        }
    }
}