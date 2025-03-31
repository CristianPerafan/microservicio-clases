package com.icesi.microservicio_clase.config;

import com.icesi.microservicio_clase.dto.DatosEntrenamiento;
import com.icesi.microservicio_clase.dto.ResumenEntrenamiento;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Bean
    public KStream<String, DatosEntrenamiento> kStream(StreamsBuilder streamsBuilder) {
        JsonSerde<DatosEntrenamiento> datosEntrenamientoSerde = new JsonSerde<>(DatosEntrenamiento.class);
        JsonSerde<ResumenEntrenamiento> resumenEntrenamientoSerde = new JsonSerde<>(ResumenEntrenamiento.class);

        KStream<String, DatosEntrenamiento> stream = streamsBuilder.stream(
                "datos-entrenamiento",
                Consumed.with(Serdes.String(), datosEntrenamientoSerde)
        );

        stream.groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofDays(7)))
                .aggregate(
                        ResumenEntrenamiento::new,
                        (key, value, aggregate) -> aggregate.actualizar(value),
                        Materialized.<String, ResumenEntrenamiento, WindowStore<Bytes, byte[]>>as("resumen-entrenamiento-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(resumenEntrenamientoSerde)
                )
                .toStream()
                .map((windowedKey, value) -> new KeyValue<>(windowedKey.key(), value)) // Extraer solo el miembroId como clave
                .to("resumen-entrenamiento", Produced.with(Serdes.String(), resumenEntrenamientoSerde));

        return stream;
    }
}