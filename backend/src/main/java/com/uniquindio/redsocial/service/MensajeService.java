package com.uniquindio.redsocial.service;

import com.uniquindio.redsocial.exception.MensajeException;
import com.uniquindio.redsocial.model.Conversacion;
import com.uniquindio.redsocial.model.Mensaje;
import com.uniquindio.redsocial.model.Usuario;
import com.uniquindio.redsocial.repository.ConversacionRepository;
import com.uniquindio.redsocial.repository.MensajeRepository;
import com.uniquindio.redsocial.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MensajeService {

    private final MensajeRepository mensajeRepo;
    private final UsuarioRepository usuarioRepo;
    private final ConversacionRepository conversacionRepo;

    public Mensaje enviarMensaje(Long remitenteId, Long conversacionId, String contenido) {
        validarParametros(remitenteId, conversacionId, contenido);

        Usuario remitente = obtenerUsuario(remitenteId);
        Conversacion conversacion = obtenerConversacion(conversacionId);
        validarParticipacion(remitente, conversacion);

        Mensaje mensaje = crearMensaje(remitente, conversacion, contenido);
        return mensajeRepo.save(mensaje);
    }

    public List<Mensaje> obtenerMensajesPorConversacion(Long conversacionId) {
        if (conversacionId == null || conversacionId <= 0) {
            throw new MensajeException("ID de conversación inválido");
        }

        Conversacion conversacion = obtenerConversacion(conversacionId);
        return mensajeRepo.findByConversacionOrderByFechaEnvioAsc(conversacion);
    }

    public void eliminarMensaje(String mensajeId) {
        if (mensajeId == null || mensajeId.trim().isEmpty()) {
            throw new MensajeException("ID de mensaje inválido");
        }

        try {
            Long id = Long.parseLong(mensajeId);
            mensajeRepo.deleteById(id);
        } catch (NumberFormatException e) {
            throw new MensajeException("ID de mensaje inválido");
        } catch (Exception e) {
            throw new MensajeException("Error al eliminar el mensaje: " + e.getMessage());
        }
    }

    public Page<Mensaje> buscarMensajes(Usuario usuario, String keyword, Pageable pageable) {
        if (usuario == null || keyword == null) {
            throw new MensajeException("Parámetros de búsqueda inválidos");
        }
        return mensajeRepo.searchMessages(usuario, keyword, pageable);
    }

    public void marcarMensajesComoLeidos(Long conversacionId, Long usuarioId) {
        Conversacion conversacion = obtenerConversacion(conversacionId);
        Usuario usuario = obtenerUsuario(usuarioId);
        mensajeRepo.markMessagesAsRead(conversacion, usuario);
    }

    public List<Mensaje> obtenerMensajesNoLeidos(Long usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        return mensajeRepo.findUnreadMessagesByUser(usuario);
    }

    public long contarMensajesNoLeidos(Long conversacionId, Long usuarioId) {
        Conversacion conversacion = obtenerConversacion(conversacionId);
        Usuario usuario = obtenerUsuario(usuarioId);
        return mensajeRepo.countUnreadMessages(conversacion, usuario);
    }

    private void validarParametros(Long remitenteId, Long conversacionId, String contenido) {
        if (remitenteId == null || remitenteId <= 0) {
            throw new MensajeException("ID de remitente inválido");
        }
        if (conversacionId == null || conversacionId <= 0) {
            throw new MensajeException("ID de conversación inválido");
        }
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new MensajeException("El contenido del mensaje no puede estar vacío");
        }
        if (contenido.length() > 1000) {
            throw new MensajeException("El contenido del mensaje excede el límite de 1000 caracteres");
        }
    }

    private Usuario obtenerUsuario(Long usuarioId) {
        return usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new MensajeException("Usuario no encontrado"));
    }

    private Conversacion obtenerConversacion(Long conversacionId) {
        return conversacionRepo.findById(conversacionId)
                .orElseThrow(() -> new MensajeException("Conversación no encontrada"));
    }

    private void validarParticipacion(Usuario usuario, Conversacion conversacion) {
        if (!conversacion.getParticipantes().contains(usuario)) {
            throw new MensajeException("El usuario no está autorizado para enviar mensajes en esta conversación");
        }
    }

    private Mensaje crearMensaje(Usuario remitente, Conversacion conversacion, String contenido) {
        return new Mensaje(
                contenido.trim(),
                remitente,
                conversacion
        );
    }
}