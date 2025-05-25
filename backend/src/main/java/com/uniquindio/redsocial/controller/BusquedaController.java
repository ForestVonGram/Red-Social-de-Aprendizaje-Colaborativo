package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Contenido;
import com.uniquindio.redsocial.service.BusquedaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buscar")
@CrossOrigin(origins = "*")
@Validated
@Tag(name = "Búsqueda", description = "API para realizar búsquedas de contenido usando árbol")
public class BusquedaController {

    private final BusquedaService busquedaService;

    @Autowired
    public BusquedaController(BusquedaService busquedaService) {
        this.busquedaService = busquedaService;
    }

    @Operation(summary = "Buscar contenido por título",
            description = "Realiza búsqueda de contenido utilizando un árbol de búsqueda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda exitosa"),
            @ApiResponse(responseCode = "400", description = "Título de búsqueda inválido"),
            @ApiResponse(responseCode = "404", description = "No se encontraron resultados")
    })
    @GetMapping("/titulo")
    public ResponseEntity<List<Contenido>> buscarPorTitulo(
            @Parameter(description = "Título a buscar", required = true)
            @RequestParam @NotBlank(message = "El título no puede estar vacío") String titulo) {

        List<Contenido> resultados = busquedaService.buscarPorTitulo(titulo);

        if (resultados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resultados);
    }
}