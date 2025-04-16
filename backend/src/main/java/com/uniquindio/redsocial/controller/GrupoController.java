package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.service.GrupoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
@RequiredArgsConstructor
public class GrupoController {

    private final GrupoService grupoService;

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarUsuario(@RequestBody Usuario usuario) {
        grupoService.asignarUsuarioAGrupo(usuario);
        return ResponseEntity.ok("Usuario agregado al grupo");
    }

    @DeleteMapping("/eliminar/{correo}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable String correo) {
        grupoService.eliminarUsuarioDelGrupo(correo);
        return ResponseEntity.ok("Usuario eliminado del grupo");
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarGrupo() {
        return ResponseEntity.ok(grupoService.listarGrupo());
    }
}