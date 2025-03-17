package com.icesi.microservicio_clase.producer;


import com.icesi.microservicio_clase.dto.OcupacionClase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OcupacionClasesProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OcupacionClasesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarOcupacionClase(Long idClase, int ocupacion) {

        try {
            OcupacionClase ocupacionClase = new OcupacionClase(String.valueOf(idClase), ocupacion);
            kafkaTemplate.send("ocupacion.clases", String.valueOf(idClase), "Ocupación de clase: " + ocupacionClase.getOcupacion());
        } catch (Exception e) {
            System.out.println("Error enviando mensaje de ocupación de clase: " + e.getMessage());
        }
    }


}
