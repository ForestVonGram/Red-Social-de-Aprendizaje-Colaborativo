package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.GrafoUsuariosService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/grafo")
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
}
