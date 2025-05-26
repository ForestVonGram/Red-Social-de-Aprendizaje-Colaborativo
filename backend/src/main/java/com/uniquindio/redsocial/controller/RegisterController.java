package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.dto.RegisterDTO;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.UsuarioService;
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

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@Tag(name = "Registro", description = "API para el registro de nuevos usuarios en el sistema")
public class RegisterController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Registrar nuevo usuario",
            description = "Registra un nuevo usuario en el sistema con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos"),
            @ApiResponse(responseCode = "409", description = "El correo electrónico ya está registrado")
    })
    @PostMapping
    public ResponseEntity<Map<String, String>> registrar(
            @Parameter(description = "Datos del usuario a registrar", required = true)
            @Valid @RequestBody RegisterDTO dto) {
        try {
            validarRegistroDTO(dto);
            Usuario usuarioRegistrado = usuarioService.registrar(dto);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Usuario registrado correctamente");
            response.put("correo", usuarioRegistrado.getCorreo());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("ya está en uso")) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al registrar el usuario");
        }
    }

    private void validarRegistroDTO(RegisterDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos de registro no pueden ser nulos");
        }

        if (dto.getCorreo() == null || dto.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio");
        }

        if (!dto.getCorreo().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El correo electrónico no es válido");
        }

        if (dto.getContrasenia() == null || dto.getContrasenia().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        if (dto.getContrasenia().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }

        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        // Validar que el nombre tenga al menos dos palabras
        String[] palabrasNombre = dto.getNombre().trim().split("\\s+");
        if (palabrasNombre.length < 2) {
            throw new IllegalArgumentException("Debe proporcionar el nombre completo");
        }

        if (dto.getIntereses() == null || dto.getIntereses().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un interés");
        }
    }

    @Operation(summary = "Verificar disponibilidad de correo",
            description = "Verifica si un correo electrónico está disponible para registro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Correo electrónico inválido")
    })
    @GetMapping("/verificar-correo/{correo}")
    public ResponseEntity<Map<String, Object>> verificarCorreo(
            @Parameter(description = "Correo electrónico a verificar", required = true)
            @PathVariable String correo) {
        try {
            boolean disponible = usuarioService.verificarCorreoDisponible(correo);

            Map<String, Object> response = new HashMap<>();
            response.put("disponible", disponible);
            response.put("correo", correo);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al verificar el correo");
        }
    }
}