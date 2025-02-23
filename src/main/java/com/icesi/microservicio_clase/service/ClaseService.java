package com.icesi.microservicio_clase.service;


import com.icesi.microservicio_clase.exception.EntrenadorNoEncontradoException;
import com.icesi.microservicio_clase.model.Clase;
import com.icesi.microservicio_clase.repository.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClaseService {
    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Clase> obtenerClases(){
        return claseRepository.findAll();
    }

    public Clase crearClase(Clase clase){
        try {
            System.out.println("Guardando clase: "+clase.getEntrenadorID());
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8081/api/entrenador/"+clase.getEntrenadorID().getEntrenadorId(), String.class);

            HttpStatusCode statusCode = response.getStatusCode();
            System.out.println("Status code: "+statusCode);

            return claseRepository.save(clase);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Entrenador no encontrado");
        } catch (ResourceAccessException e) {
            return claseRepository.save(clase);
        } catch (Exception e) {
            // Imprimir el tipo de la excepción
            System.out.println("Excepción capturada: " + e.getClass().getName());
            System.out.println("Error: "+e.getMessage());
            throw new ResponseStatusException(HttpStatusCode.valueOf(500));
        }
    }
}
