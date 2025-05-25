package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.model.Conversacion;
import com.uniquindio.redsocial.model.Mensaje;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.repository.MensajeRepository;
import com.uniquindio.redsocial.repository.ConversacionRepository;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MensajeService {

    private final MensajeRepository mensajeRepo;
    private final ConversacionRepository conversacionRepo;
    private final UsuarioRepository usuarioRepo;

    public Mensaje enviarMensaje(Long remitenteId, Long conversacionId, String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del mensaje no puede estar vacío");
        }

        Usuario remitente = usuarioRepo.findById(remitenteId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Conversacion conversacion = conversacionRepo.findById(conversacionId)
                .orElseThrow(() -> new IllegalArgumentException("Conversación no encontrada"));

        if (!conversacion.getParticipantes().contains(remitente)) {
            throw new SecurityException("El usuario no está autorizado para enviar mensajes en esta conversación");
        }

        Mensaje mensaje = new Mensaje();
        mensaje.setId(UUID.randomUUID().toString());
        mensaje.setConversacion(conversacion);
        mensaje.setRemitente(remitente);
        mensaje.setContenido(contenido.trim());
        mensaje.setFecha(LocalDateTime.now());

        return mensajeRepo.save(mensaje);
    }

    public List<Mensaje> obtenerMensajesPorConversacion(Long conversacionId) {
        Conversacion conversacion = conversacionRepo.findById(conversacionId)
                .orElseThrow(() -> new IllegalArgumentException("Conversación no encontrada"));

        return mensajeRepo.findByConversacionOrderByFechaAsc(conversacion);
    }

    public void eliminarMensaje(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID de mensaje inválido");
        }

        if (!mensajeRepo.existsById(id)) {
            throw new IllegalArgumentException("Mensaje no encontrado");
        }

        mensajeRepo.deleteById(id);
    }
}