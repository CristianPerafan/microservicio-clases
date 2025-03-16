package com.icesi.microservicio_clase.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Getter
@Setter
public class Clase {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private LocalDateTime horario;
    private  int capacidadMaxima;

    @ElementCollection
    private Set<Long> miembros;

    private EntrenadorID entrenadorID;
}
