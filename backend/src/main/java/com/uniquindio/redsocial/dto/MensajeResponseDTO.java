package com.uniquindio.redsocial.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.uniquindio.redsocial.model.Mensaje;
import com.uniquindio.redsocial.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para la respuesta de mensajes")
public class MensajeResponseDTO {

    @Schema(description = "ID del mensaje", example = "1")
    private Long id;

    @Schema(description = "Contenido del mensaje", example = "¡Hola! ¿Cómo estás?")
    private String contenido;

    @Schema(description = "Usuario remitente del mensaje")
    private UsuarioSimpleDTO remitente;

    @Schema(description = "Fecha y hora del mensaje")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fecha;

    @Schema(description = "Estado de lectura del mensaje")
    private boolean leido;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioSimpleDTO {
        private Long id;
        private String nombre;
        private String correo;
    }

    public static MensajeResponseDTO fromEntity(Mensaje mensaje) {
        if (mensaje == null) {
            return null;
        }

        MensajeResponseDTO dto = new MensajeResponseDTO();
        dto.setId(mensaje.getId());
        dto.setContenido(mensaje.getContenido());
        dto.setFecha(mensaje.getFechaEnvio());
        dto.setLeido(mensaje.isLeido());

        Usuario emisor = mensaje.getEmisor();
        if (emisor != null) {
            UsuarioSimpleDTO usuarioDTO = new UsuarioSimpleDTO(
                    emisor.getId(),
                    emisor.getNombre(),
                    emisor.getCorreo()
            );
            dto.setRemitente(usuarioDTO);
        }

        return dto;
    }
}