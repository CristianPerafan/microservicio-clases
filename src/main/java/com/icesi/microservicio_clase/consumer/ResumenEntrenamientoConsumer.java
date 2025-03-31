package com.icesi.microservicio_clase.consumer;

import com.icesi.microservicio_clase.dto.ResumenEntrenamiento;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResumenEntrenamientoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ResumenEntrenamientoConsumer.class);
    private final Map<String, ResumenEntrenamiento> resumenes = new HashMap<>();

    public void consumirResumen(ConsumerRecord<String, ResumenEntrenamiento> record) {
        String miembroId = record.key();
        ResumenEntrenamiento resumen = record.value();
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
        return new HashMap<>();
    }
}