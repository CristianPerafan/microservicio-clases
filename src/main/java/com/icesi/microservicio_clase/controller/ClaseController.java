package com.icesi.microservicio_clase.controller;

import com.icesi.microservicio_clase.model.Clase;
import com.icesi.microservicio_clase.service.ClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clase")
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @GetMapping
    public List<Clase> obtenerClases(){
        return null;
    }

}
