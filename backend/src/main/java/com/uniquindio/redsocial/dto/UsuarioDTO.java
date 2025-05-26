package com.uniquindio.redsocial.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para la gestión de usuarios")
public class UsuarioDTO {

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$",
            message = "El nombre solo debe contener letras y espacios")
    private String nombre;

    @Schema(description = "Correo electrónico del usuario",
            example = "juan.perez@email.com")
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 255, message = "El correo electrónico no puede exceder los 255 caracteres")
    private String correo;

    @Schema(description = "Contraseña del usuario (solo para creación/actualización)")
    @JsonIgnore
    private String contrasenia;

    @Schema(description = "Lista de intereses del usuario")
    @Size(max = 10, message = "No puede tener más de 10 intereses")
    private List<@NotBlank(message = "Los intereses no pueden estar vacíos")
    @Size(min = 2, max = 50, message = "Cada interés debe tener entre 2 y 50 caracteres")
            String> intereses = new ArrayList<>();

    @Schema(description = "Fecha de registro del usuario")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaRegistro;

    @Schema(description = "Estado del usuario (activo/inactivo)")
    private boolean activo = true;

    @Schema(description = "Nivel de experiencia del usuario", example = "1")
    @Min(value = 1, message = "El nivel mínimo es 1")
    @Max(value = 100, message = "El nivel máximo es 100")
    private Integer nivel = 1;

    @Schema(description = "IDs de las conversaciones en las que participa")
    private Set<Long> conversacionesIds = new HashSet<>();

    public UsuarioDTO(String nombre, String correo, String contrasenia) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.fechaRegistro = LocalDateTime.now();
        this.nivel = 1;
    }

    public void sanitizarDatos() {
        if (nombre != null) {
            nombre = nombre.trim();
        }
        if (correo != null) {
            correo = correo.trim().toLowerCase();
        }
        if (intereses != null) {
            intereses = intereses.stream()
                    .filter(interes -> interes != null && !interes.trim().isEmpty())
                    .map(String::trim)
                    .distinct()
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }

    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() &&
                correo != null && correo.matches("^[A-Za-z0-9+_.-]+@(.+)$") &&
                (id != null || (contrasenia != null && esContraseniaValida())) &&
                (intereses == null || intereses.size() <= 10) &&
                (nivel >= 1 && nivel <= 100);
    }

    private boolean esContraseniaValida() {
        if (contrasenia == null) {
            return false;
        }

        return contrasenia.length() >= 8 &&
                contrasenia.length() <= 30 &&
                contrasenia.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
    }

    public void agregarInteres(String interes) {
        if (interes != null && !interes.trim().isEmpty() &&
                (intereses == null || intereses.size() < 10)) {
            if (intereses == null) {
                intereses = new ArrayList<>();
            }
            String interesLimpio = interes.trim();
            if (!intereses.contains(interesLimpio)) {
                intereses.add(interesLimpio);
            }
        }
    }

    public void agregarConversacion(Long conversacionId) {
        if (conversacionId != null && conversacionId > 0) {
            if (conversacionesIds == null) {
                conversacionesIds = new HashSet<>();
            }
            conversacionesIds.add(conversacionId);
        }
    }

    public void incrementarNivel() {
        if (nivel < 100) {
            nivel++;
        }
    }

    public void cambiarEstado(boolean nuevoEstado) {
        this.activo = nuevoEstado;
    }

    @Override
    public String toString() {
        return String.format(
                "Usuario{id=%d, nombre='%s', correo='%s', nivel=%d, activo=%b, " +
                        "intereses=%d, conversaciones=%d}",
                id, nombre, correo, nivel, activo,
                intereses != null ? intereses.size() : 0,
                conversacionesIds != null ? conversacionesIds.size() : 0
        );
    }
}