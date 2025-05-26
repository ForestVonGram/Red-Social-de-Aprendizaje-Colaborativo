package com.uniquindio.redsocial.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para la transferencia de datos de conversaciones")
public class ConversacionDTO {

    @Schema(description = "Identificador único de la conversación", example = "1")
    private Long id;

    @Schema(description = "Lista de correos de los participantes en la conversación",
            example = "[\"usuario1@email.com\", \"usuario2@email.com\"]")
    @NotEmpty(message = "Debe incluir al menos dos participantes en la conversación")
    @Size(min = 2, message = "Una conversación debe tener al menos dos participantes")
    private List<String> participantes;

    @Schema(description = "Cantidad de mensajes en la conversación", example = "10")
    private Integer cantidadMensajes;

    @Schema(description = "Indica si la conversación está activa",
            example = "true", defaultValue = "true")
    private boolean activa = true;

    public ConversacionDTO(List<String> participantes) {
        this.participantes = participantes;
        this.cantidadMensajes = 0;
    }

    public boolean esValida() {
        if (participantes == null || participantes.size() < 2) {
            return false;
        }

        long participantesUnicos = participantes.stream()
                .distinct()
                .count();

        if (participantesUnicos != participantes.size()) {
            return false;
        }

        return participantes.stream()
                .allMatch(correo -> correo != null &&
                        correo.matches("^[A-Za-z0-9+_.-]+@(.+)$"));
    }

    public void actualizarCantidadMensajes(int cantidad) {
        this.cantidadMensajes = cantidad;
    }

    public boolean esParticipante(String correoUsuario) {
        return participantes != null &&
                participantes.contains(correoUsuario);
    }

    public void agregarParticipante(String correoUsuario) {
        if (participantes != null &&
                !participantes.contains(correoUsuario) &&
                correoUsuario.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            participantes.add(correoUsuario);
        }
    }

    public void removerParticipante(String correoUsuario) {
        if (participantes != null &&
                participantes.size() > 2) {
            participantes.remove(correoUsuario);
        }
    }
}