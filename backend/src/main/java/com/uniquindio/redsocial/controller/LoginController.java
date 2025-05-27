package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.dto.LoginDTO;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.UsuarioService;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Validated
@Tag(name = "Autenticación", description = "API para la gestión de autenticación de usuarios")
public class LoginController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Iniciar sesión",
            description = "Autentica un usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "400", description = "Datos de inicio de sesión inválidos")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> login(
            @Parameter(description = "Credenciales de usuario", required = true)
            @Valid @RequestBody LoginDTO loginDTO) {
        try {
            if (loginDTO.getCorreo() == null || loginDTO.getCorreo().trim().isEmpty()) {
                throw new IllegalArgumentException("El correo es requerido");
            }
            if (loginDTO.getContrasenia() == null || loginDTO.getContrasenia().trim().isEmpty()) {
                throw new IllegalArgumentException("La contraseña es requerida");
            }

            boolean autenticado = usuarioService.autenticar(loginDTO.getCorreo(), loginDTO.getContrasenia());

            if (!autenticado) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(crearRespuesta(false, "Credenciales inválidas"));
            }

            Usuario usuario = usuarioService.buscarPorCorreo(loginDTO.getCorreo())
                    .orElseThrow(() -> new IllegalStateException("Usuario no encontrado después de autenticación"));

            Map<String, Object> respuesta = crearRespuesta(true, "Inicio de sesión exitoso");
            respuesta.put("usuario", usuario);

            return ResponseEntity.ok(respuesta);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error durante el inicio de sesión", e);
        }
    }

    @Operation(summary = "Obtener información de usuario",
            description = "Obtiene la información de un usuario por su correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Correo electrónico inválido")
    })
    @GetMapping("/{correo}")
    public ResponseEntity<Usuario> obtenerUsuario(
            @Parameter(description = "Correo electrónico del usuario", required = true)
            @PathVariable @NotBlank @Email(message = "Debe proporcionar un correo electrónico válido")
            String correo) {
        try {
            return usuarioService.buscarPorCorreo(correo)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Usuario no encontrado"));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener la información del usuario", e);
        }
    }

    private Map<String, Object> crearRespuesta(boolean exitoso, String mensaje) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("exitoso", exitoso);
        respuesta.put("mensaje", mensaje);
        return respuesta;
    }
}