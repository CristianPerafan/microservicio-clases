package com.icesi.microservicio_clase.producer;

import com.icesi.microservicio_clase.dto.DatosEntrenamiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DatosEntrenamientoProducer {

    @Autowired
    private KafkaTemplate<String, DatosEntrenamiento> kafkaTemplate;

    public void enviarDatosEntrenamiento(Long miembroId, String tipoEjercicio, int duracionMinutos, int caloriasQuemadas) {
        DatosEntrenamiento datos = new DatosEntrenamiento(miembroId, tipoEjercicio, duracionMinutos, caloriasQuemadas, LocalDateTime.now());
        kafkaTemplate.send("datos-entrenamiento", miembroId.toString(), datos);
        System.out.println("Datos de entrenamiento enviados - Miembro: " + miembroId + ", Tipo: " + tipoEjercicio);
    }
}
