package com.icesi.microservicio_clase.controller;

import com.icesi.microservicio_clase.consumer.ResumenEntrenamientoConsumer;
import com.icesi.microservicio_clase.dto.CambioHorarioDTO;
import com.icesi.microservicio_clase.dto.ClaseDTO;
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
import java.util.Map;

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

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Crear una nueva clase")
    @ApiResponse(responseCode = "201", description = "Clase creada con éxito")
    public Clase crearClase(@RequestBody Clase clase) {
        return claseService.crearClase(clase);
    }

    @GetMapping("/public")
    @Operation(summary = "Acceder a recurso público")
    @ApiResponse(responseCode = "200", description = "Recurso público accedido con éxito")
    public String publico() {
        return "Este es un recurso público";
    }

    @PostMapping("/{id}/inscribir")
    @Operation(summary = "Inscribir un miembro a una clase")
    @ApiResponse(responseCode = "200", description = "Miembro inscrito a la clase")
    @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    public String inscribirMiembro(@PathVariable Long id, @RequestBody InscripcionDTO inscripcionDTO) {
        System.out.println("Inscribiendo miembro a clase con id: " + id);
        return claseService.inscribirMiembro(id, inscripcionDTO);
    }

    @PostMapping("/{id}/cambiar-horario")
    @Operation(summary = "Cambiar horario de una clase")
    @ApiResponse(responseCode = "200", description = "Horario cambiado con éxito")
    @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    public Clase cambiarHorario(@PathVariable Long id, @RequestBody CambioHorarioDTO horario) {
        return claseService.cambiarHorario(id, horario);
    }

    @GetMapping("/resumen-entrenamiento/{miembroId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    @Operation(summary = "Obtener el resumen de entrenamiento de un miembro")
    @ApiResponse(responseCode = "200", description = "Resumen obtenido con éxito")
    @ApiResponse(responseCode = "404", description = "Resumen no encontrado para el miembro")
    public ResponseEntity<ResumenEntrenamiento> obtenerResumenEntrenamiento(@PathVariable String miembroId) {
        ResumenEntrenamiento resumen = resumenEntrenamientoConsumer.obtenerResumen(miembroId);
        System.out.println("Resumen obtenido: " + resumen);
        if (resumen == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/resumen-entrenamiento")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    @Operation(summary = "Obtener todos los resúmenes de entrenamiento")
    @ApiResponse(responseCode = "200", description = "Lista de resúmenes obtenida con éxito")
    public ResponseEntity<Map<String, ResumenEntrenamiento>> obtenerTodosResumenesEntrenamiento() {
        return ResponseEntity.ok(resumenEntrenamientoConsumer.obtenerTodosResumenes());
    }

    @GetMapping("/clases-miembro/{miembroId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    @Operation(summary = "Obtener clases de un miembro")
    @ApiResponse(responseCode = "200", description = "Lista de clases obtenida con éxito")
    @ApiResponse(responseCode = "404", description = "Miembro no encontrado")
    public ResponseEntity<List<ClaseDTO>> obtenerClasesPorMiembro(@PathVariable Long miembroId) {
        List<ClaseDTO> clases = claseService.obtenerClasesPorMiembro(miembroId);
        if (clases == null || clases.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clases);
    }
}
