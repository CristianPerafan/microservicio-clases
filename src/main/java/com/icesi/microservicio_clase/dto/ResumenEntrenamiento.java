package com.icesi.microservicio_clase.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResumenEntrenamiento implements Serializable {
    private int totalDuracionMinutos;
    private int totalCaloriasQuemadas;
    private int sesiones;

    public ResumenEntrenamiento() {
        this.totalDuracionMinutos = 0;
        this.totalCaloriasQuemadas = 0;
        this.sesiones = 0;
    }

    public ResumenEntrenamiento actualizar(DatosEntrenamiento datos) {
        this.totalDuracionMinutos += datos.getDuracionMinutos();
        this.totalCaloriasQuemadas += datos.getCaloriasQuemadas();
        this.sesiones += 1;
        return this;
    }
}