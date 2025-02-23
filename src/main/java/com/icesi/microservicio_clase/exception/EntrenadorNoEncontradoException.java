package com.icesi.microservicio_clase.exception;

import com.icesi.microservicio_clase.model.EntrenadorID;

public class EntrenadorNoEncontradoException extends RuntimeException {
    public EntrenadorNoEncontradoException(EntrenadorID entrenadorID) {
        super("El entrenador con ID " + entrenadorID.getEntrenadorId() + " no se encuentra.");
    }
}
