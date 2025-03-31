package com.icesi.microservicio_clase.consumer;

import com.icesi.microservicio_clase.dto.ResumenEntrenamiento;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Component
public class ResumenEntrenamientoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ResumenEntrenamientoConsumer.class);
    private final Map<String, ResumenEntrenamiento> resumenes = new HashMap<>();

    @KafkaListener(topics = "resumen-entrenamiento", groupId = "test-group")
    public void consumirResumen(ConsumerRecord<String, ResumenEntrenamiento> record) {
        System.out.println("Mensaje recibido de resumen-entrenamiento: " + record);
        String miembroId = record.key();
        ResumenEntrenamiento resumen = record.value();
        System.out.println("Miembro: " + miembroId + ", Resumen: " + resumen);
        resumenes.put(miembroId, resumen);
        logger.info("Resumen almacenado para miembro {}: {}", miembroId, resumen);
    }

    public ResumenEntrenamiento obtenerResumen(String miembroId) {
        ResumenEntrenamiento resumen = resumenes.get(miembroId);
        logger.debug("Consultando resumen para miembro {}: {}", miembroId, resumen);
        return resumen;
    }

    public Map<String, ResumenEntrenamiento> obtenerTodosResumenes() {
        logger.debug("Consultando todos los resúmenes: {}", resumenes);
        return new HashMap<>(resumenes);
    }
}
