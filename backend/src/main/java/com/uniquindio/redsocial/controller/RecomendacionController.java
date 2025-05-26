package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.RecomendacionService;
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

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/recomendaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@Tag(name = "Recomendaciones", description = "API para el sistema de recomendación de usuarios")
public class RecomendacionController {

    private final RecomendacionService recomendacionService;

    @Operation(summary = "Obtener recomendaciones de usuarios",
            description = "Obtiene una lista de usuarios recomendados basada en las conexiones y preferencias del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recomendaciones obtenidas correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay recomendaciones disponibles"),
            @ApiResponse(responseCode = "400", description = "Correo electrónico inválido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{correo}")
    public ResponseEntity<?> recomendar(
            @Parameter(description = "Correo electrónico del usuario", required = true)
            @PathVariable @NotBlank @Email(message = "Debe proporcionar un correo electrónico válido")
            String correo) {
        try {
            Set<Usuario> recomendaciones = recomendacionService.obtenerRecomendaciones(correo);

            if (recomendaciones.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No hay recomendaciones disponibles para este usuario");
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(recomendaciones);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener las recomendaciones");
        }
    }

    @Operation(summary = "Registrar nuevo usuario en el sistema de recomendaciones",
            description = "Registra un nuevo usuario en el grafo de recomendaciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del usuario inválidos"),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe")
    })
    @PostMapping("/registrar")
    public ResponseEntity<Map<String, String>> registrarUsuario(
            @Parameter(description = "Datos del usuario a registrar", required = true)
            @RequestBody @Validated Usuario usuario) {
        try {
            recomendacionService.registrarUsuario(usuario);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Usuario registrado correctamente en el sistema de recomendaciones");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al registrar el usuario");
        }
    }

    @Operation(summary = "Conectar usuarios",
            description = "Establece una conexión entre dos usuarios en el sistema de recomendaciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conexión establecida correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de conexión inválidos"),
            @ApiResponse(responseCode = "404", description = "Uno o ambos usuarios no encontrados")
    })
    @PostMapping("/conectar")
    public ResponseEntity<Map<String, String>> conectar(
            @Parameter(description = "Correo del primer usuario", required = true)
            @RequestParam @NotBlank @Email String correo1,
            @Parameter(description = "Correo del segundo usuario", required = true)
            @RequestParam @NotBlank @Email String correo2) {
        try {
            recomendacionService.conectar(correo1, correo2);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Usuarios conectados correctamente");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al conectar los usuarios");
        }
    }
}