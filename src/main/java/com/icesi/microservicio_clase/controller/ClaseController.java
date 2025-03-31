package com.icesi.microservicio_clase.controller;

import com.icesi.microservicio_clase.consumer.ResumenEntrenamientoConsumer;
import com.icesi.microservicio_clase.dto.CambioHorarioDTO;
import com.icesi.microservicio_clase.dto.InscripcionDTO;
import com.icesi.microservicio_clase.dto.ResumenEntrenamiento;
import com.icesi.microservicio_clase.model.Clase;
import com.icesi.microservicio_clase.service.ClaseService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clase")
@Tag(name = "Clase", description = "API para la gestión de clases")
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @Autowired
    private ResumenEntrenamientoConsumer resumenEntrenamientoConsumer;

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

    @PostMapping("/{id}/inscribir")
    public String inscribirMiembro(@PathVariable Long id, @RequestBody InscripcionDTO inscripcionDTO) {
        return claseService.inscribirMiembro(id, inscripcionDTO);
    }

    @PostMapping("/{id}/cambiar-horario")
    public Clase cambiarHorario(@PathVariable Long id, @RequestBody CambioHorarioDTO horario) {
        return claseService.cambiarHorario(id, horario);
    }

    @GetMapping("/resumen-entrenamiento/{miembroId}")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    @Operation(summary = "Obtener el resumen de entrenamiento de un miembro")
    @ApiResponse(responseCode = "200", description = "Resumen obtenido con éxito")
    @ApiResponse(responseCode = "404", description = "Resumen no encontrado para el miembro")
    public ResponseEntity<ResumenEntrenamiento> obtenerResumenEntrenamiento(@PathVariable String miembroId) {
        ResumenEntrenamiento resumen = resumenEntrenamientoConsumer.obtenerResumen(miembroId);
        if (resumen == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resumen);
    }


}
