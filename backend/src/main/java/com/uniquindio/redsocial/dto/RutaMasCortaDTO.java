package com.uniquindio.redsocial.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para la búsqueda de la ruta más corta entre dos usuarios")
public class RutaMasCortaDTO {

    @Schema(description = "Correo electrónico del usuario origen",
            example = "usuario1@email.com")
    @NotBlank(message = "El correo de origen es obligatorio")
    @Email(message = "El formato del correo de origen no es válido")
    private String correoOrigen;

    @Schema(description = "Correo electrónico del usuario destino",
            example = "usuario2@email.com")
    @NotBlank(message = "El correo de destino es obligatorio")
    @Email(message = "El formato del correo de destino no es válido")
    private String correoDestino;

    @Schema(description = "Lista de usuarios que forman la ruta encontrada")
    private List<UsuarioDTO> ruta;

    @Schema(description = "Longitud de la ruta (número de conexiones)")
    private Integer longitud;

    public RutaMasCortaDTO(String correoOrigen, String correoDestino) {
        this.correoOrigen = correoOrigen;
        this.correoDestino = correoDestino;
        this.ruta = new ArrayList<>();
    }

    public boolean esValido() {
        if (correoOrigen == null || correoDestino == null ||
                correoOrigen.trim().isEmpty() || correoDestino.trim().isEmpty()) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!correoOrigen.matches(emailRegex) || !correoDestino.matches(emailRegex)) {
            return false;
        }

        return !correoOrigen.equals(correoDestino);
    }

    public void sanitizarDatos() {
        if (correoOrigen != null) {
            correoOrigen = correoOrigen.trim().toLowerCase();
        }
        if (correoDestino != null) {
            correoDestino = correoDestino.trim().toLowerCase();
        }
    }

    public void agregarUsuarioARuta(UsuarioDTO usuario) {
        if (usuario != null) {
            if (ruta == null) {
                ruta = new ArrayList<>();
            }
            ruta.add(usuario);
            actualizarLongitud();
        }
    }

    private void actualizarLongitud() {
        this.longitud = ruta != null ? ruta.size() - 1 : 0;
    }

    public void limpiarRuta() {
        if (ruta != null) {
            ruta.clear();
        }
        longitud = 0;
    }

    public boolean existeRuta() {
        return ruta != null && !ruta.isEmpty() && longitud > 0;
    }

    public List<String> obtenerRutaComoCorreos() {
        if (ruta == null || ruta.isEmpty()) {
            return new ArrayList<>();
        }
        return ruta.stream()
                .map(UsuarioDTO::getCorreo)
                .toList();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RutaMasCortaDTO{")
                .append("origen='").append(correoOrigen).append('\'')
                .append(", destino='").append(correoDestino).append('\'');

        if (existeRuta()) {
            sb.append(", longitud=").append(longitud)
                    .append(", ruta=").append(obtenerRutaComoCorreos());
        }

        sb.append('}');
        return sb.toString();
    }
}