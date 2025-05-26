package com.uniquindio.redsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "conversacion")
public class Conversacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Debe haber al menos dos participantes en la conversación")
    @Size(min = 2, message = "Una conversación debe tener al menos dos participantes")
    @ManyToMany
    @JoinTable(
            name = "conversacion_usuarios",
            joinColumns = @JoinColumn(name = "conversacion_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonIgnoreProperties({"conversaciones", "password", "valoraciones"})
    private List<Usuario> participantes = new ArrayList<>();

    @OneToMany(
            mappedBy = "conversacion",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("fechaEnvio ASC")
    @JsonIgnoreProperties("conversacion")
    private List<Mensaje> mensajes = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "ultima_actividad")
    private LocalDateTime ultimaActividad;

    @Column(name = "esta_activa", nullable = false)
    private boolean estaActiva = true;

    // Métodos de utilidad
    public void agregarParticipante(Usuario usuario) {
        if (usuario != null && !participantes.contains(usuario)) {
            participantes.add(usuario);
        }
    }

    public void removerParticipante(Usuario usuario) {
        participantes.remove(usuario);
    }

    public void agregarMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
        mensaje.setConversacion(this);
        this.ultimaActividad = LocalDateTime.now();
    }

    public void removerMensaje(Mensaje mensaje) {
        mensajes.remove(mensaje);
        mensaje.setConversacion(null);
    }

    public Mensaje obtenerUltimoMensaje() {
        if (mensajes.isEmpty()) {
            return null;
        }
        return mensajes.get(mensajes.size() - 1);
    }

    public boolean tieneParticipante(Usuario usuario) {
        return participantes.contains(usuario);
    }

    // Constructor personalizado
    public Conversacion(List<Usuario> participantes) {
        this.participantes = new ArrayList<>(participantes);
        this.mensajes = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
        this.ultimaActividad = LocalDateTime.now();
    }
}