package com.icesi.microservicio_clase;

import com.icesi.microservicio_clase.model.Clase;
import com.icesi.microservicio_clase.model.EntrenadorID;
import com.icesi.microservicio_clase.repository.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClaseRepository claseRepository;
    @Override
    public void run(String... args) throws Exception {

        Clase clase1 = new Clase();
        clase1.setNombre("Yoga");
        clase1.setHorario(LocalDate.of(2021, 5, 10).atStartOfDay());
        clase1.setCapacidadMaxima(20);
        clase1.setEntrenadorID(new EntrenadorID(1L));
        claseRepository.save(clase1);

        Clase clase2 = new Clase();
        clase2.setNombre("Spinning");
        clase2.setHorario(LocalDate.of(2021, 5, 10).atStartOfDay());
        clase2.setCapacidadMaxima(20);
        clase2.setEntrenadorID(new EntrenadorID(2L));
        claseRepository.save(clase2);


    }
}
