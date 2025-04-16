package com.uniquindio.redsocial.controller;

import com.uniquindio.redsocial.model.GrupoEstudio;
import com.uniquindio.redsocial.service.GrupoEstudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos/automaticos")
@RequiredArgsConstructor
public class GrupoEstudioController {
    private final GrupoEstudioService grupoEstudioService;

    @GetMapping("/formar")
    public ResponseEntity<List<GrupoEstudio>> formarGrupos() {
        List<GrupoEstudio> grupos = grupoEstudioService.formarGruposPorIntereses();
        return ResponseEntity.ok(grupos);
    }
}