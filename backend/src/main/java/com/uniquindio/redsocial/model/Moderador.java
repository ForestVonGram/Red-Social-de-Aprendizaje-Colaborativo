package com.uniquindio.redsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "moderador")
public class Moderador implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El formato del correo no es válido")
    @Column(unique = true, nullable = false)
    private String correo;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "La contraseña debe contener al menos un número, una letra minúscula, una letra mayúscula y un carácter especial")
    @Column(nullable = false)
    private String contrasenia;

    @OneToMany(mappedBy = "moderadorAsignado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("moderadorAsignado")
    private List<Solicitud> solicitudesAsignadas = new ArrayList<>();

    @OneToMany(mappedBy = "moderadorAsignado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("moderadorAsignado")
    private List<SolicitudAyuda> solicitudesAyudaAsignadas = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "ultima_actividad")
    private LocalDateTime ultimaActividad;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "intentos_fallidos")
    private int intentosFallidos = 0;

    // Métodos de utilidad
    public void asignarSolicitud(Solicitud solicitud) {
        solicitudesAsignadas.add(solicitud);
        solicitud.setModeradorAsignado(this);
    }

    public void asignarSolicitudAyuda(SolicitudAyuda solicitud) {
        solicitudesAyudaAsignadas.add(solicitud);
        solicitud.setModeradorAsignado(this);
    }

    public void removerSolicitud(Solicitud solicitud) {
        solicitudesAsignadas.remove(solicitud);
        solicitud.setModeradorAsignado(null);
    }

    public void removerSolicitudAyuda(SolicitudAyuda solicitud) {
        solicitudesAyudaAsignadas.remove(solicitud);
        solicitud.setModeradorAsignado(null);
    }

    // Implementación de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_MODERATOR"));
    }

    @Override
    public String getPassword() {
        return contrasenia;
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return intentosFallidos < 3;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }

    // Constructor personalizado
    public Moderador(String nombre, String correo, String contrasenia) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.solicitudesAsignadas = new ArrayList<>();
        this.solicitudesAyudaAsignadas = new ArrayList<>();
    }
}