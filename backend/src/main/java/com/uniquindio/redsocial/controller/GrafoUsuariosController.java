package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.GrafoUsuariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/grafo")
@CrossOrigin(origins = "*")
@Validated
@RequiredArgsConstructor
@Tag(name = "Grafo de Usuarios", description = "API para gestionar las conexiones entre usuarios")
public class GrafoUsuariosController {

    private final GrafoUsuariosService grafoUsuariosService;

    @Operation(summary = "Agregar usuario al grafo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario agregado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos")
    })
    @PostMapping("/usuario")
    public ResponseEntity<Void> agregarUsuario(@Valid @RequestBody Usuario usuario) {
        try {
            grafoUsuariosService.agregarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Agregar conexión entre usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conexión creada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario(s) no encontrado(s)")
    })
    @PostMapping("/conexion")
    public ResponseEntity<Void> agregarConexion(
            @Parameter(description = "Correo del primer usuario", required = true)
            @RequestParam @NotBlank String correo1,
            @Parameter(description = "Correo del segundo usuario", required = true)
            @RequestParam @NotBlank String correo2) {
        try {
            grafoUsuariosService.agregarConexion(correo1, correo2);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Eliminar conexión entre usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conexión eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Conexión no encontrada")
    })
    @DeleteMapping("/conexion")
    public ResponseEntity<Void> eliminarConexion(
            @Parameter(description = "Correo del primer usuario", required = true)
            @RequestParam @NotBlank String correo1,
            @Parameter(description = "Correo del segundo usuario", required = true)
            @RequestParam @NotBlank String correo2) {
        try {
            grafoUsuariosService.eliminarConexion(correo1, correo2);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Verificar si dos usuarios están conectados")
    @GetMapping("/estan-conectados")
    public ResponseEntity<Boolean> estanConectados(
            @Parameter(description = "Correo del primer usuario", required = true)
            @RequestParam @NotBlank String correo1,
            @Parameter(description = "Correo del segundo usuario", required = true)
            @RequestParam @NotBlank String correo2) {
        try {
            return ResponseEntity.ok(grafoUsuariosService.estanConectados(correo1, correo2));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Obtener amigos de un usuario")
    @GetMapping("/amigos/{correo}")
    public ResponseEntity<Set<Usuario>> obtenerAmigos(
            @Parameter(description = "Correo del usuario", required = true)
            @PathVariable @NotBlank String correo) {
        try {
            Set<Usuario> amigos = grafoUsuariosService.obtenerAmigos(correo);
            return amigos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(amigos);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Obtener recomendaciones de amigos")
    @GetMapping("/recomendaciones/{correo}")
    public ResponseEntity<Set<Usuario>> recomendarAmigos(
            @Parameter(description = "Correo del usuario", required = true)
            @PathVariable @NotBlank String correo) {
        try {
            Set<Usuario> recomendaciones = grafoUsuariosService.recomendarAmigos(correo);
            return recomendaciones.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(recomendaciones);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Buscar ruta más corta entre dos usuarios")
    @GetMapping("/ruta")
    public ResponseEntity<List<Usuario>> buscarRutaMasCorta(
            @Parameter(description = "Correo del usuario origen", required = true)
            @RequestParam @NotBlank String origen,
            @Parameter(description = "Correo del usuario destino", required = true)
            @RequestParam @NotBlank String destino) {
        try {
            List<Usuario> ruta = grafoUsuariosService.buscarRutaMasCorta(origen, destino);
            return ruta.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(ruta);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Obtener usuarios con más conexiones")
    @GetMapping("/top-conectados")
    public ResponseEntity<List<Usuario>> obtenerUsuariosConMasConexiones(
            @Parameter(description = "Cantidad de usuarios a retornar", required = true)
            @RequestParam(defaultValue = "5") @Min(1) int top) {
        try {
            List<Usuario> topUsuarios = grafoUsuariosService.obtenerUsuariosConMasConexiones(top);
            return topUsuarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(topUsuarios);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Sugerir compañeros por intereses comunes")
    @GetMapping("/sugerencias-intereses/{correo}")
    public ResponseEntity<List<Usuario>> sugerirCompanerosPorIntereses(
            @Parameter(description = "Correo del usuario", required = true)
            @PathVariable @NotBlank String correo) {
        try {
            List<Usuario> sugerencias = grafoUsuariosService.sugerirCompanerosPorIntereses(correo);
            return sugerencias.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(sugerencias);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Visualizar grafo de conexiones")
    @GetMapping("/visualizar")
    public ResponseEntity<Void> visualizarGrafo() {
        try {
            grafoUsuariosService.visualizarGrafo();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al visualizar el grafo");
        }
    }

    @Operation(summary = "Obtener estructura del grafo")
    @GetMapping("/estructura")
    public ResponseEntity<Map<String, Object>> obtenerEstructuraGrafo() {
        try {
            Map<String, Object> estructuraGrafo = new HashMap<>();

            // Obtener y formatear nodos
            List<Map<String, String>> nodos = grafoUsuariosService.obtenerTodosLosUsuarios().stream()
                    .map(usuario -> {
                        Map<String, String> nodo = new HashMap<>();
                        nodo.put("id", usuario.getCorreo());
                        nodo.put("label", usuario.getNombre());
                        return nodo;
                    })
                    .toList();

            // Obtener y formatear enlaces
            List<Map<String, String>> enlaces = new ArrayList<>();
            Map<String, Set<String>> conexiones = grafoUsuariosService.obtenerTodasLasConexiones();
            conexiones.forEach((usuario, conexionesUsuario) ->
                    conexionesUsuario.forEach(conexion -> {
                        Map<String, String> enlace = new HashMap<>();
                        enlace.put("source", usuario);
                        enlace.put("target", conexion);
                        enlaces.add(enlace);
                    })
            );

            estructuraGrafo.put("nodes", nodos);
            estructuraGrafo.put("links", enlaces);

            return ResponseEntity.ok(estructuraGrafo);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener la estructura del grafo");
        }
    }
}