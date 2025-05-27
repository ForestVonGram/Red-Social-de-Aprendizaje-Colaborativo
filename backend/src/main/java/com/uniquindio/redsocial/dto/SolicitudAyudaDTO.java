package com.uniquindio.redsocial.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.uniquindio.redsocial.model.SolicitudAyuda;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para la gestión de solicitudes de ayuda")
public class SolicitudAyudaDTO {

    @Schema(description = "ID de la solicitud de ayuda", example = "1")
    private Long id;

    @Schema(description = "ID del usuario solicitante", example = "1")
    @NotNull(message = "El ID del solicitante es obligatorio")
    @Positive(message = "El ID del solicitante debe ser positivo")
    private Long solicitanteId;

    @Schema(description = "Título de la solicitud",
            example = "Ayuda con ejercicios de programación")
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 5, max = 100, message = "El título debe tener entre 5 y 100 caracteres")
    private String titulo;

    @Schema(description = "Descripción detallada de la solicitud")
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 20, max = 1000, message = "La descripción debe tener entre 20 y 1000 caracteres")
    private String descripcion;

    @Schema(description = "Nivel de prioridad", example = "ALTA")
    @NotNull(message = "La prioridad es obligatoria")
    private SolicitudAyuda.Prioridad prioridad;


    @Schema(description = "Materias o temas relacionados")
    @Size(min = 1, max = 5, message = "Debe especificar entre 1 y 5 materias")
    private List<@NotBlank(message = "La materia no puede estar vacía")
    @Size(min = 2, max = 50, message = "La materia debe tener entre 2 y 50 caracteres")
            String> materias = new ArrayList<>();

    @Schema(description = "Estado actual de la solicitud")
    @Pattern(regexp = "^(PENDIENTE|EN_PROCESO|COMPLETADA|CANCELADA)$",
            message = "Estado inválido. Valores permitidos: PENDIENTE, EN_PROCESO, COMPLETADA, CANCELADA")
    private String estado;

    @Schema(description = "Fecha de creación de la solicitud")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha límite para resolver la solicitud")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "La fecha límite debe ser futura")
    private LocalDateTime fechaLimite;

    @Schema(description = "ID del ayudante asignado (si existe)", example = "2")
    private Long ayudanteId;

    public SolicitudAyudaDTO(Long solicitanteId, String titulo, String descripcion,
                             SolicitudAyuda.Prioridad prioridad, List<String> materias) {
        this.solicitanteId = solicitanteId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.materias = materias != null ? new ArrayList<>(materias) : new ArrayList<>();
        this.estado = "PENDIENTE";
        this.fechaCreacion = LocalDateTime.now();
        this.fechaLimite = LocalDateTime.now().plusDays(7); // Por defecto 7 días
    }

    public void sanitizarDatos() {
        if (titulo != null) {
            titulo = titulo.trim();
        }
        if (descripcion != null) {
            descripcion = descripcion.trim();
        }
        if (materias != null) {
            materias = materias.stream()
                    .filter(materia -> materia != null && !materia.trim().isEmpty())
                    .map(String::trim)
                    .distinct()
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }

    public boolean esValida() {
        return solicitanteId != null && solicitanteId > 0 &&
                titulo != null && !titulo.trim().isEmpty() &&
                descripcion != null && !descripcion.trim().isEmpty() &&
                prioridad != null &&
                materias != null && !materias.isEmpty() && materias.size() <= 5 &&
                fechaLimite != null && fechaLimite.isAfter(LocalDateTime.now());
    }

    public void actualizarEstado(String nuevoEstado) {
        if (nuevoEstado != null &&
                nuevoEstado.matches("^(PENDIENTE|EN_PROCESO|COMPLETADA|CANCELADA)$")) {
            this.estado = nuevoEstado;
        } else {
            throw new IllegalArgumentException("Estado inválido");
        }
    }

    public void asignarAyudante(Long ayudanteId) {
        if (ayudanteId != null && ayudanteId > 0) {
            this.ayudanteId = ayudanteId;
            this.estado = "EN_PROCESO";
        } else {
            throw new IllegalArgumentException("ID de ayudante inválido");
        }
    }

    public long calcularTiempoRestante() {
        if (fechaLimite == null ||
                estado.equals("COMPLETADA") ||
                estado.equals("CANCELADA")) {
            return 0;
        }
        return java.time.Duration.between(LocalDateTime.now(), fechaLimite).toHours();
    }

    @Override
    public String toString() {
        return String.format(
                "SolicitudAyuda{id=%d, titulo='%s', prioridad=%s, estado='%s', " +
                        "tiempoRestante=%d horas}",
                id, titulo, prioridad, estado, calcularTiempoRestante()
        );
    }
}