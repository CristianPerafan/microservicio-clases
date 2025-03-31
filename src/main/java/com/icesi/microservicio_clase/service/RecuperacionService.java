package com.icesi.microservicio_clase.service;

import com.icesi.microservicio_clase.consumer.ResumenEntrenamientoConsumer;
import com.icesi.microservicio_clase.dto.ResumenEntrenamiento;
import com.icesi.microservicio_clase.model.OffsetCheckpoint;
import com.icesi.microservicio_clase.repository.OffsetCheckpointRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RecuperacionService {

    private static final Logger logger = LoggerFactory.getLogger(RecuperacionService.class);

    @Autowired
    private ConsumerFactory<String, ResumenEntrenamiento> resumenConsumerFactory;

    @Autowired
    private OffsetCheckpointRepository offsetRepository;

    @Autowired
    private ResumenEntrenamientoConsumer resumenEntrenamientoConsumer;

    @PostConstruct
    public void iniciarProcesamiento() {
        logger.info("Iniciando procesamiento de recuperación en hilo separado");
        new Thread(this::procesarMensajesKafka).start();
    }

    private void procesarMensajesKafka() {
        try (KafkaConsumer<String, ResumenEntrenamiento> consumer = (KafkaConsumer<String, ResumenEntrenamiento>) resumenConsumerFactory.createConsumer()) {
            consumer.subscribe(Arrays.asList("resumen-entrenamiento"));
            logger.info("Suscrito al topic resumen-entrenamiento");

            // Forzar asignación de particiones con un poll inicial
            ConsumerRecords<String, ResumenEntrenamiento> initialRecords = consumer.poll(Duration.ofMillis(1000));
            Set<TopicPartition> assignedPartitions = consumer.assignment();
            logger.info("Asignación de particiones completada: {}", assignedPartitions);

            // Cargar los últimos offsets procesados
            Map<TopicPartition, Long> ultimoOffsetProcesado = cargarUltimoOffset();
            logger.info("Offsets cargados desde la base de datos: {}", ultimoOffsetProcesado);

            if (!ultimoOffsetProcesado.isEmpty()) {
                ultimoOffsetProcesado.forEach((partition, offset) -> {
                    if (assignedPartitions.contains(partition)) {
                        logger.info("Aplicando seek a partición {} con offset {}", partition, offset);
                        consumer.seek(partition, offset);
                    } else {
                        logger.warn("Partición {} no asignada al consumidor, omitiendo seek", partition);
                    }
                });
            } else {
                logger.info("No hay offsets previos, comenzando desde el principio");
                if (!assignedPartitions.isEmpty()) {
                    consumer.seekToBeginning(assignedPartitions);
                } else {
                    logger.warn("No hay particiones asignadas aún, esperando mensajes");
                }
            }

            while (true) {
                ConsumerRecords<String, ResumenEntrenamiento> records = consumer.poll(Duration.ofMillis(100));
                if (!records.isEmpty()) {
                    logger.info("Recibidos {} registros de resumen-entrenamiento", records.count());
                    for (ConsumerRecord<String, ResumenEntrenamiento> record : records) {
                        logger.debug("Procesando registro: topic={}, partition={}, offset={}, key={}",
                                record.topic(), record.partition(), record.offset(), record.key());
                        procesarRecord(record);
                        guardarOffset(record.topic(), record.partition(), record.offset());
                    }
                    consumer.commitSync();
                    logger.debug("Offsets confirmados");
                } else {
                    logger.debug("No hay nuevos registros en esta iteración");
                }
            }
        } catch (Exception e) {
            logger.error("Error en el procesamiento de Kafka: {}", e.getMessage(), e);
        }
    }

    private void procesarRecord(ConsumerRecord<String, ResumenEntrenamiento> record) {
        resumenEntrenamientoConsumer.consumirResumen(record);
    }

    private Map<TopicPartition, Long> cargarUltimoOffset() {
        Map<TopicPartition, Long> offsets = new HashMap<>();
        offsetRepository.findAll().forEach(checkpoint -> {
            TopicPartition tp = new TopicPartition(checkpoint.getTopic(), checkpoint.getPartition());
            offsets.put(tp, checkpoint.getOffsetValue() + 1);
        });
        return offsets;
    }

    private void guardarOffset(String topic, int partition, long offset) {
        OffsetCheckpoint checkpoint = new OffsetCheckpoint(topic, partition, offset);
        offsetRepository.save(checkpoint);
    }
}