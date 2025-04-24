package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.repository.ContenidoRepository;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ParticipacionService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContenidoRepository contenidoRepository;

    @Autowired
    private GrafoUsuariosService grafoUsuariosService;

    public Map<String, Map<String, Integer>> obtenerNivelesDeParticipacion() {
        Map<String, Map<String, Integer>> nivelesParticipacion = new HashMap<>();

        for (Usuario usuario : usuarioRepository.findAll()) {
            String correo = usuario.getCorreo();

            int publicaciones = (int) contenidoRepository.findAll().stream()
                    .filter(contenido -> contenido.getAutor().getCorreo().equals(correo))
                    .count();

            int valoraciones = (int) contenidoRepository.findAll().stream()
                    .flatMap(contenido -> contenido.getValoraciones().stream())
                    .filter(valoracion -> valoracion.getContenido().getAutor().getCorreo().equals(correo))
                    .count();

            int conexiones = grafoUsuariosService.obtenerAmigos(correo).size();

            Map<String, Integer> participacion = new HashMap<>();
            participacion.put("publicaciones", publicaciones);
            participacion.put("valoraciones", valoraciones);
            participacion.put("conexiones", conexiones);

            nivelesParticipacion.put(correo, participacion);
        }

        return nivelesParticipacion;
    }
}
