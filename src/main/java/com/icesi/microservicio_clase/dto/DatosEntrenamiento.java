package com.icesi.microservicio_clase.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DatosEntrenamiento implements Serializable {
    private Long miembroId;
    private String tipoEjercicio; // "cardio", "fuerza", etc
    private int duracionMinutos;
    private int caloriasQuemadas;
    private LocalDateTime timestamp;

    public DatosEntrenamiento() {}

    public DatosEntrenamiento(Long miembroId, String tipoEjercicio, int duracionMinutos, int caloriasQuemadas, LocalDateTime timestamp) {
        this.miembroId = miembroId;
        this.tipoEjercicio = tipoEjercicio;
        this.duracionMinutos = duracionMinutos;
        this.caloriasQuemadas = caloriasQuemadas;
        this.timestamp = timestamp;
    }
}