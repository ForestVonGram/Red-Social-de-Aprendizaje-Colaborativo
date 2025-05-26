package com.uniquindio.redsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mensaje")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    @Column(nullable = false, length = 1000)
    private String contenido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emisor_id", nullable = false)
    @NotNull(message = "El emisor es requerido")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario emisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversacion_id", nullable = false)
    @NotNull(message = "La conversación es requerida")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "mensajes"})
    private Conversacion conversacion;

    @CreationTimestamp
    @Column(name = "fecha_envio", nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    private boolean leido;

    // Constructor personalizado
    public Mensaje(String contenido, Usuario emisor, Conversacion conversacion) {
        this.contenido = contenido;
        this.emisor = emisor;
        this.conversacion = conversacion;
        this.leido = false;
    }
}