package com.icesi.microservicio_clase.service;


import com.icesi.microservicio_clase.model.Clase;
import com.icesi.microservicio_clase.repository.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaseService {
    @Autowired
    private ClaseRepository claseRepository;

    public List<Clase> obtenerClases(){
        return claseRepository.findAll();
    }
}
