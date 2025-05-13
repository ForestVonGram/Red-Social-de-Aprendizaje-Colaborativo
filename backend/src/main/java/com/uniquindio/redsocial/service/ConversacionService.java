package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.model.Conversacion;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.repository.ConversacionRepository;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversacionService {

    private final ConversacionRepository conversacionRepo;
    private final UsuarioRepository usuarioRepo;

    public Conversacion crearConversacion(List<String> idsParticipantes) {
        List<Usuario> participantes = usuarioRepo.findAllById(idsParticipantes);

        if (participantes.size() < 2){
            throw new IllegalArgumentException("Se requieren al menos dos participantes para iniciar la conversación.");
        }

        Conversacion conversacion = new Conversacion();
        conversacion.setId(Long.valueOf(UUID.randomUUID().toString()));
        conversacion.setParticipantes(participantes);

        return conversacionRepo.save(conversacion);
    }

    public Optional<Conversacion> obtenerConversacion(String id){
        return conversacionRepo.findById(id);
    }
}
