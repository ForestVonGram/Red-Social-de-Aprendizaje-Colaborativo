package com.uniquindio.redsocial.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para la autenticación de usuarios")
public class LoginDTO {

    @Schema(description = "Correo electrónico del usuario",
            example = "usuario@email.com")
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido",
            regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String correo;

    @Schema(description = "Contraseña del usuario",
            example = "Contraseña123!")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 30, message = "La contraseña debe tener entre 8 y 30 caracteres")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "La contraseña debe contener al menos un número, una letra mayúscula, " +
                    "una letra minúscula y un carácter especial (@#$%^&+=!)")
    private String contrasenia;

    public boolean esCorreoValido() {
        return correo != null &&
                correo.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public boolean esContraseniaValida() {
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

    public void sanitizarDatos() {
        if (correo != null) {
            correo = correo.trim().toLowerCase();
        }
    }

    public boolean estanDatosCompletos() {
        return correo != null && !correo.trim().isEmpty() &&
                contrasenia != null && !contrasenia.trim().isEmpty();
    }

    public LoginDTO(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "correo='" + correo + '\'' +
                ", contrasenia='********'" +
                '}';
    }
}