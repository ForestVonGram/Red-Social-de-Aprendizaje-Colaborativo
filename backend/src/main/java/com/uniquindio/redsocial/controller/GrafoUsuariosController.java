package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.GrafoUsuariosService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/grafo")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@Validated
@Tag(name = "Grafo de Usuarios", description = "API para gestionar las conexiones entre usuarios\n")
public class GrafoUsuariosController {
    private final GrafoUsuariosService grafoUsuariosService;

    public GrafoUsuariosController(GrafoUsuariosService grafoUsuariosService) {
        this.grafoUsuariosService = grafoUsuariosService;
    }

    @PostMapping("/usuario")
    public void agregarUsuario(@RequestBody Usuario usuario) {
        grafoUsuariosService.agregarUsuario(usuario);
    }

    @PostMapping("/conexion")
    public void agregarConexion(@RequestParam String correo1, @RequestParam String correo2) {
        grafoUsuariosService.agregarConexion(correo1, correo2);
    }

    @DeleteMapping("/conexion")
    public void eliminarConexion(@RequestParam String correo1, @RequestParam String correo2) {
        grafoUsuariosService.eliminarConexion(correo1, correo2);
    }

    @GetMapping("/estan-conectados")
    public boolean estanConectados(@RequestParam String correo1, @RequestParam String correo2) {
        return grafoUsuariosService.estanConectados(correo1, correo2);
    }

    @GetMapping("/amigos/{correo}")
    public Set<Usuario> obtenerAmigos(@PathVariable String correo) {
        return grafoUsuariosService.obtenerAmigos(correo);
    }

    @GetMapping("/recomendaciones/{correo}")
    public Set<Usuario> recomendarAmigos(@PathVariable String correo) {
        return grafoUsuariosService.recomendarAmigos(correo);
    }

    @GetMapping("/ruta")
    public List<Usuario> buscarRutaMasCorta(@RequestParam String origen, @RequestParam String destino) {
        return grafoUsuariosService.buscarRutaMasCorta(origen, destino);
    }

    @GetMapping("/top-conectados")
    public List<Usuario> obtenerUsuariosConMasConexiones(@RequestParam(defaultValue = "5") int top) {
        return grafoUsuariosService.obtenerUsuariosConMasConexiones(top);
    }

    @GetMapping("/sugerencias-intereses/{correo}")
    public List<Usuario> sugerirCompanerosPorIntereses(@PathVariable String correo) {
        return grafoUsuariosService.sugerirCompanerosPorIntereses(correo);
    }

    @GetMapping("/visualizar")
    public void visualizarGrafo() {
        grafoUsuariosService.visualizarGrafo();
    }

    @GetMapping("/estructura")
    public Map<String, Object> obtenerEstructuraGrafo() {
        Map<String, Object> estructuraGrafo = new HashMap<>();

        List<Map<String, String>> nodos = new ArrayList<>();
        for (Usuario usuario : grafoUsuariosService.obtenerTodosLosUsuarios()) {
            Map<String, String> nodo = new HashMap<>();
            nodo.put("id", usuario.getCorreo());
            nodo.put("label", usuario.getNombre());
            nodos.add(nodo);
        }

        List<Map<String, String>> enlaces = new ArrayList<>();
        for (String usuario : grafoUsuariosService.obtenerTodasLasConexiones().keySet()) {
            for (String conexion : grafoUsuariosService.obtenerTodasLasConexiones().get(usuario)) {
                Map<String, String> enlace = new HashMap<>();
                enlace.put("source", usuario);
                enlace.put("target", conexion);
                enlaces.add(enlace);
            }
        }

        estructuraGrafo.put("nodes", nodos);
        estructuraGrafo.put("links", enlaces);
        return estructuraGrafo;
    }
}
