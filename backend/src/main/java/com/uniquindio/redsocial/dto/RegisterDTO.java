package com.uniquindio.redsocial.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para el registro de nuevos usuarios")
public class RegisterDTO {

    @Schema(description = "ID del usuario (generado automáticamente)",
            accessMode = Schema.AccessMode.READ_ONLY)
    @JsonIgnore
    private Long id;

    @Schema(description = "Nombre completo del usuario",
            example = "Juan Pérez")
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

    @Schema(description = "Contraseña del usuario",
            example = "Contraseña123!")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 30, message = "La contraseña debe tener entre 8 y 30 caracteres")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "La contraseña debe contener al menos un número, una letra mayúscula, " +
                    "una letra minúscula y un carácter especial (@#$%^&+=!)")
    private String contrasenia;

    @Schema(description = "Lista de intereses del usuario",
            example = "[\"Programación\", \"Matemáticas\", \"Física\"]")
    @Size(max = 10, message = "No puede tener más de 10 intereses")
    private List<@NotBlank(message = "Los intereses no pueden estar vacíos")
    @Size(min = 2, max = 50, message = "Cada interés debe tener entre 2 y 50 caracteres")
            String> intereses = new ArrayList<>();

    public RegisterDTO(String nombre, String correo, String contrasenia) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.intereses = new ArrayList<>();
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

    public boolean esRegistroValido() {
        return nombre != null && !nombre.trim().isEmpty() &&
                correo != null && correo.matches("^[A-Za-z0-9+_.-]+@(.+)$") &&
                contrasenia != null && esContraseniaValida() &&
                (intereses == null || intereses.size() <= 10);
    }

    private boolean esContraseniaValida() {
        if (contrasenia == null) {
            return false;
        }

        boolean tieneMinuscula = contrasenia.matches(".*[a-z].*");
        boolean tieneMayuscula = contrasenia.matches(".*[A-Z].*");
        boolean tieneNumero = contrasenia.matches(".*[0-9].*");
        boolean tieneEspecial = contrasenia.matches(".*[@#$%^&+=!].*");
        boolean longitudValida = contrasenia.length() >= 8 && contrasenia.length() <= 30;
        boolean sinEspacios = !contrasenia.contains(" ");

        return tieneMinuscula && tieneMayuscula && tieneNumero &&
                tieneEspecial && longitudValida && sinEspacios;
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

    @Override
    public String toString() {
        return "RegisterDTO{" +
                "nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", contrasenia='********'" +
                ", intereses=" + intereses +
                '}';
    }
}