package com.icesi.microservicio_clase.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Miembro{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
