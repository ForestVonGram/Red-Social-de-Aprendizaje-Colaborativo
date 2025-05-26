package com.uniquindio.redsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "grupo_estudio")
public class GrupoEstudio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tema central no puede estar vacío")
    @Size(min = 3, max = 100, message = "El tema central debe tener entre 3 y 100 caracteres")
    @Column(name = "tema_central", nullable = false)
    private String temaCentral;

    @NotEmpty(message = "El grupo debe tener al menos un miembro")
    @ManyToMany
    @JoinTable(
            name = "grupo_estudio_miembros",
            joinColumns = @JoinColumn(name = "grupo_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonIgnoreProperties({"grupos", "password", "valoraciones"})
    private List<Usuario> miembros = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lider_id")
    @JsonIgnoreProperties({"gruposLiderados", "password"})
    private Usuario lider;

    @Column(name = "capacidad_maxima")
    private int capacidadMaxima = 50;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "ultima_actividad")
    private LocalDateTime ultimaActividad;

    @Column(name = "esta_activo", nullable = false)
    private boolean estaActivo = true;

    @ElementCollection
    @CollectionTable(
            name = "grupo_estudio_temas_relacionados",
            joinColumns = @JoinColumn(name = "grupo_id")
    )
    @Column(name = "tema")
    private List<String> temasRelacionados = new ArrayList<>();

    // Métodos de utilidad
    public boolean agregarMiembro(Usuario usuario) {
        if (usuario != null && !miembros.contains(usuario) && miembros.size() < capacidadMaxima) {
            miembros.add(usuario);
            ultimaActividad = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public boolean removerMiembro(Usuario usuario) {
        boolean removido = miembros.remove(usuario);
        if (removido) {
            ultimaActividad = LocalDateTime.now();
        }
        return removido;
    }

    public boolean estaLleno() {
        return miembros.size() >= capacidadMaxima;
    }

    public boolean contieneMiembro(Usuario usuario) {
        return miembros.contains(usuario);
    }

    public void agregarTemaRelacionado(String tema) {
        if (tema != null && !tema.trim().isEmpty() && !temasRelacionados.contains(tema)) {
            temasRelacionados.add(tema);
        }
    }

    public void removerTemaRelacionado(String tema) {
        temasRelacionados.remove(tema);
    }

    public GrupoEstudio(String temaCentral, Usuario lider) {
        this.temaCentral = temaCentral;
        this.lider = lider;
        this.miembros = new ArrayList<>();
        this.temasRelacionados = new ArrayList<>();
        this.miembros.add(lider); // El líder es automáticamente miembro del grupo
        this.fechaCreacion = LocalDateTime.now();
        this.ultimaActividad = LocalDateTime.now();
    }
}