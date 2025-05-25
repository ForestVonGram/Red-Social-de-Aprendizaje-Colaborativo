package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.model.Conversacion;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.repository.ConversacionRepository;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConversacionService {

    private final ConversacionRepository conversacionRepo;
    private final UsuarioRepository usuarioRepo;

    public Conversacion crearConversacion(List<Long> idsParticipantes) {
        List<Usuario> participantes = usuarioRepo.findAllById(idsParticipantes);

        if (participantes.size() < 2) {
            throw new IllegalArgumentException("Se requieren al menos dos participantes para iniciar la conversación.");
        }

        Conversacion conversacion = new Conversacion();
        conversacion.setParticipantes(participantes);

        return conversacionRepo.save(conversacion);
    }

    public Optional<Conversacion> obtenerConversacion(Long id) {
        return conversacionRepo.findById(id);
    }

    public List<Conversacion> obtenerConversacionesPorUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return conversacionRepo.findByParticipantesContains(usuario);
    }

    public boolean eliminarConversacion(Long id) {
        if (!conversacionRepo.existsById(id)) {
            return false;
        }

        try {
            conversacionRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la conversación", e);
        }
    }
}