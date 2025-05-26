package com.uniquindio.redsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contenido")
public class Contenido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    @Column(nullable = false, length = 500)
    private String descripcion;

    @NotNull(message = "El tipo de contenido es requerido")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoContenido tipo;

    @NotBlank(message = "La URL no puede estar vacía")
    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    @NotNull(message = "El autor es requerido")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario autor;

    @CreationTimestamp
    @Column(name = "fecha_publicacion", nullable = false, updatable = false)
    private LocalDateTime fechaPublicacion;

    @OneToMany(mappedBy = "contenido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("contenido")
    private List<Valoracion> valoraciones = new ArrayList<>();

    @Transient
    private Double promedioValoraciones;

    public enum TipoContenido {
        VIDEO, IMAGEN, DOCUMENTO
    }

    // Métodos de utilidad
    public void agregarValoracion(Valoracion valoracion) {
        valoraciones.add(valoracion);
        valoracion.setContenido(this);
    }

    public void removerValoracion(Valoracion valoracion) {
        valoraciones.remove(valoracion);
        valoracion.setContenido(null);
    }

    public Double calcularPromedioValoraciones() {
        if (valoraciones == null || valoraciones.isEmpty()) {
            return 0.0;
        }
        return valoraciones.stream()
                .mapToDouble(Valoracion::getEstrellas)
                .average()
                .orElse(0.0);
    }

    @PrePersist
    protected void onCreate() {
        fechaPublicacion = LocalDateTime.now();
    }

    @PostLoad
    protected void onLoad() {
        promedioValoraciones = calcularPromedioValoraciones();
    }

    public Contenido(String titulo, String descripcion, TipoContenido tipo, String url, Usuario autor) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.url = url;
        this.autor = autor;
        this.valoraciones = new ArrayList<>();
    }
}