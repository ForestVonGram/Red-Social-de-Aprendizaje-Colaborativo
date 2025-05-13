package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.model.Conversacion;
import com.uniquindio.redsocial.model.Mensaje;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.repository.MensajeRepository;
import com.uniquindio.redsocial.repository.ConversacionRepository;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import lombok.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MensajeService {

    private final MensajeRepository mensajeRepo;
    private final ConversacionRepository conversacionRepo;
    private final UsuarioRepository usuarioRepo;

    public Mensaje enviarMensaje(String remitenteId, String conversacionId, String contenido) {
        Usuario remitente = usuarioRepo.findById(remitenteId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Conversacion conversacion = conversacionRepo.findById(conversacionId)
                .orElseThrow(() -> new IllegalArgumentException("Conversación no encontrada"));

        if (!conversacion.getParticipantes().contains(remitente)) {
            throw new IllegalArgumentException("El usuario no es parte de la conversación");
        }

        Mensaje mensaje = new Mensaje();
        mensaje.setId(UUID.randomUUID().toString());
        mensaje.setConversacion(conversacion);
        mensaje.setRemitente(remitente);
        mensaje.setContenido(contenido);
        mensaje.setFecha(LocalDateTime.now());
        return mensajeRepo.save(mensaje);
    }

    public List<Mensaje> obtenerMensajesPorConversacion(String conversacionId) {
        Conversacion conversacion = conversacionRepo.findById(conversacionId)
                .orElseThrow(() -> new IllegalArgumentException("Conversación no encontrada"));
        return mensajeRepo.findByConversacionOrderByFechaAsc(conversacion);
    }
}
