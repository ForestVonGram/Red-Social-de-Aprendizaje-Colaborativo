package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.dto.RutaMasCortaDTO;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.GrafoUsuariosService;
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
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/ruta-corta")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@Tag(name = "Ruta más Corta", description = "API para encontrar la ruta más corta entre dos usuarios en el grafo social")
public class RutaMasCortaController {

    private final GrafoUsuariosService grafoUsuariosService;

    @Operation(summary = "Buscar ruta más corta entre usuarios",
            description = "Encuentra la ruta más corta entre dos usuarios a través de sus conexiones en el grafo social")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruta encontrada exitosamente"),
            @ApiResponse(responseCode = "204", description = "No existe ruta entre los usuarios"),
            @ApiResponse(responseCode = "400", description = "Datos de búsqueda inválidos"),
            @ApiResponse(responseCode = "404", description = "Uno o ambos usuarios no encontrados")
    })
    @PostMapping("/buscar")
    public ResponseEntity<?> obtenerRutaMasCorta(
            @Parameter(description = "Datos para la búsqueda de la ruta", required = true)
            @Valid @RequestBody RutaMasCortaDTO dto) {
        try {
            validarDTO(dto);

            List<Usuario> ruta = grafoUsuariosService.buscarRutaMasCorta(
                    dto.getCorreoOrigen(),
                    dto.getCorreoDestino()
            );

            if (ruta.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No existe una ruta entre los usuarios especificados");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }

            Map<String, Object> response = new HashMap<>();
            List<Map<String, String>> rutaDetallada = ruta.stream()
                    .map(usuario -> {
                        Map<String, String> usuarioMap = new HashMap<>();
                        usuarioMap.put("correo", usuario.getCorreo());
                        usuarioMap.put("nombre", usuario.getNombre());
                        return usuarioMap;
                    })
                    .toList();

            response.put("ruta", rutaDetallada);
            response.put("longitud", ruta.size() - 1); // Número de conexiones necesarias
            response.put("origen", dto.getCorreoOrigen());
            response.put("destino", dto.getCorreoDestino());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("no encontrado")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al buscar la ruta más corta");
        }
    }

    @Operation(summary = "Verificar conexión entre usuarios",
            description = "Verifica si existe una conexión directa entre dos usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de verificación inválidos"),
            @ApiResponse(responseCode = "404", description = "Uno o ambos usuarios no encontrados")
    })
    @GetMapping("/verificar-conexion")
    public ResponseEntity<Map<String, Object>> verificarConexion(
            @Parameter(description = "Correo del primer usuario", required = true)
            @RequestParam String correo1,
            @Parameter(description = "Correo del segundo usuario", required = true)
            @RequestParam String correo2) {
        try {
            validarCorreos(correo1, correo2);

            boolean conectados = grafoUsuariosService.estanConectados(correo1, correo2);

            Map<String, Object> response = new HashMap<>();
            response.put("conectados", conectados);
            response.put("usuario1", correo1);
            response.put("usuario2", correo2);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al verificar la conexión");
        }
    }

    private void validarDTO(RutaMasCortaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos de búsqueda no pueden ser nulos");
        }

        validarCorreos(dto.getCorreoOrigen(), dto.getCorreoDestino());

        if (dto.getCorreoOrigen().equals(dto.getCorreoDestino())) {
            throw new IllegalArgumentException("El origen y destino no pueden ser el mismo usuario");
        }
    }

    private void validarCorreos(String correo1, String correo2) {
        if (correo1 == null || correo1.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo de origen es obligatorio");
        }

        if (correo2 == null || correo2.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo de destino es obligatorio");
        }

        if (!correo1.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El correo de origen no es válido");
        }

        if (!correo2.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El correo de destino no es válido");
        }
    }
}