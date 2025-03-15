package com.icesi.microservicio_clase.controller;

import com.icesi.microservicio_clase.model.Clase;
import com.icesi.microservicio_clase.service.ClaseService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clase")
@Tag(name = "Clase", description = "API para la gestión de clases")
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    @Operation(summary = "Obtener todas las clases")
    @ApiResponse(responseCode = "200", description = "Lista de clases obtenida con éxito")
    public List<Clase> obtenerClases() {
        return claseService.obtenerClases();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Crear una nueva clase")
    @ApiResponse(responseCode = "201", description = "Clase creada con éxito")
    public Clase crearClase(@RequestBody Clase clase) {
        return claseService.crearClase(clase);
    }

    @GetMapping("/public")
    @Operation(summary = "Acceder a recurso público")
    public String publico() {
        return "Este es un recurso público";
    }
}
