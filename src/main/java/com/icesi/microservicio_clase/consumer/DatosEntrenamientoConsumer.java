package com.icesi.microservicio_clase.consumer;

import com.icesi.microservicio_clase.dto.DatosEntrenamiento;
import com.icesi.microservicio_clase.dto.ResumenEntrenamiento;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DatosEntrenamientoConsumer {

    @KafkaListener(topics = "datos-entrenamiento", groupId = "test-group")
    public void escuchar(DatosEntrenamiento datos) {
        System.out.println("Mensaje recibido: " + datos);
    }

    @KafkaListener(topics = "resumen-entrenamiento", groupId = "test-group")
    public void escucharResumen(ResumenEntrenamiento datos) {
        System.out.println("Mensaje recibido: " + datos);
    }
}