package com.icesi.microservicio_clase.repository;

import com.icesi.microservicio_clase.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaseRepository extends JpaRepository<Clase, Long> {
    List<Clase> findByMiembrosContains(Long miembroId);
}
