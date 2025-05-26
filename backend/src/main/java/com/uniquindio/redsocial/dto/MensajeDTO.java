package com.uniquindio.redsocial.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para la transferencia de datos de mensajes")
public class MensajeDTO {

    @Schema(description = "ID del mensaje", example = "1")
    private Long id;

    @Schema(description = "ID del remitente del mensaje", example = "1")
    @NotNull(message = "El ID del remitente es obligatorio")
    @Positive(message = "El ID del remitente debe ser un número positivo")
    private Long remitenteId;

    @Schema(description = "ID de la conversación", example = "1")
    @NotNull(message = "El ID de la conversación es obligatorio")
    @Positive(message = "El ID de la conversación debe ser un número positivo")
    private Long conversacionId;

    @Schema(description = "Contenido del mensaje", example = "¡Hola! ¿Cómo estás?")
    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    @Size(min = 1, max = 1000, message = "El contenido debe tener entre 1 y 1000 caracteres")
    private String contenido;

    @Schema(description = "Fecha y hora del mensaje")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaEnvio;

    @Schema(description = "Estado del mensaje (true si está activo, false si fue eliminado)")
    private boolean activo = true;

    public MensajeDTO(Long remitenteId, Long conversacionId, String contenido) {
        this.remitenteId = remitenteId;
        this.conversacionId = conversacionId;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now();
    }

    public void sanitizarContenido() {
        if (contenido != null) {
            contenido = contenido.trim();

            contenido = contenido.replaceAll("[<>]", "");

            if (contenido.length() > 1000) {
                contenido = contenido.substring(0, 1000);
            }
        }
    }

    public boolean esValido() {
        return remitenteId != null && remitenteId > 0 &&
                conversacionId != null && conversacionId > 0 &&
                contenido != null && !contenido.trim().isEmpty() &&
                contenido.length() <= 1000;
    }

    public void marcarComoEliminado() {
        this.activo = false;
    }

    public boolean estaActivo() {
        return activo;
    }

    public String obtenerTiempoTranscurrido() {
        if (fechaEnvio == null) {
            return "Fecha no disponible";
        }

        LocalDateTime ahora = LocalDateTime.now();
        long minutos = java.time.Duration.between(fechaEnvio, ahora).toMinutes();

        if (minutos < 1) {
            return "Ahora";
        } else if (minutos < 60) {
            return minutos + " minutos";
        } else if (minutos < 1440) {
            long horas = minutos / 60;
            return horas + " horas";
        } else {
            long dias = minutos / 1440;
            return dias + " días";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MensajeDTO{");
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        sb.append("remitenteId=").append(remitenteId)
                .append(", conversacionId=").append(conversacionId)
                .append(", contenido='").append(contenido).append('\'')
                .append(", fechaEnvio=").append(fechaEnvio)
                .append(", activo=").append(activo)
                .append('}');
        return sb.toString();
    }
}