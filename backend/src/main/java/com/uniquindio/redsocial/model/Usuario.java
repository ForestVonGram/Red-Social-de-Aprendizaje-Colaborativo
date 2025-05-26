package com.uniquindio.redsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "usuario")
public class Usuario implements UserDetails {

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

    @ElementCollection
    @CollectionTable(
            name = "usuario_intereses",
            joinColumns = @JoinColumn(name = "usuario_id")
    )
    @Column(name = "interes")
    @Size(min = 1, message = "Debe tener al menos un interés")
    private List<String> intereses = new ArrayList<>();

    @ManyToMany(mappedBy = "participantes")
    @JsonIgnoreProperties({"participantes", "mensajes"})
    private List<Conversacion> conversaciones = new ArrayList<>();

    @ManyToMany(mappedBy = "miembros")
    @JsonIgnoreProperties("miembros")
    private List<GrupoEstudio> gruposEstudio = new ArrayList<>();

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("autor")
    private List<Contenido> contenidos = new ArrayList<>();

    @OneToMany(mappedBy = "emisor", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("emisor")
    private List<Mensaje> mensajesEnviados = new ArrayList<>();

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("autor")
    private List<Valoracion> valoraciones = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @UpdateTimestamp
    @Column(name = "ultima_actividad")
    private LocalDateTime ultimaActividad;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "intentos_fallidos")
    private int intentosFallidos = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol = Rol.USUARIO;

    // Métodos de utilidad
    public void agregarInteres(String interes) {
        if (interes != null && !interes.trim().isEmpty() && !intereses.contains(interes)) {
            intereses.add(interes);
        }
    }

    public void removerInteres(String interes) {
        intereses.remove(interes);
    }

    public void agregarContenido(Contenido contenido) {
        contenidos.add(contenido);
        contenido.setAutor(this);
    }

    public void removerContenido(Contenido contenido) {
        contenidos.remove(contenido);
        contenido.setAutor(null);
    }

    public void agregarAConversacion(Conversacion conversacion) {
        if (!conversaciones.contains(conversacion)) {
            conversaciones.add(conversacion);
            conversacion.getParticipantes().add(this);
        }
    }

    public void removerDeConversacion(Conversacion conversacion) {
        if (conversaciones.remove(conversacion)) {
            conversacion.getParticipantes().remove(this);
        }
    }

    // Implementación de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
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
    public Usuario(String nombre, String correo, String contrasenia, List<String> intereses) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.intereses = intereses != null ? new ArrayList<>(intereses) : new ArrayList<>();
        this.fechaRegistro = LocalDateTime.now();
        this.ultimaActividad = LocalDateTime.now();
    }
}