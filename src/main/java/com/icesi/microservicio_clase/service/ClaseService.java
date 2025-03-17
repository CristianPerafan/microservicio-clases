package com.icesi.microservicio_clase.service;


import com.icesi.microservicio_clase.dto.CambioHorarioDTO;
import com.icesi.microservicio_clase.dto.InscripcionDTO;
import com.icesi.microservicio_clase.dto.NotificacionDTO;
import com.icesi.microservicio_clase.dto.OcupacionClase;
import com.icesi.microservicio_clase.model.Clase;
import com.icesi.microservicio_clase.producer.OcupacionClasesProducer;
import com.icesi.microservicio_clase.repository.ClaseRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OcupacionClasesProducer ocupacionClasesProducer;

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

    public String inscribirMiembro(Long id, InscripcionDTO inscripcionDTO) {
        try {
            Clase clase = claseRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Clase no encontrada"));
            if(clase.getMiembros().contains(inscripcionDTO.getMiembroId())){
                return "Miembro ya inscrito";
            }
            clase.getMiembros().add(inscripcionDTO.getMiembroId());
            claseRepository.save(clase);

            int ocupacion = clase.getMiembros().size();
            ocupacionClasesProducer.enviarOcupacionClase(id, ocupacion);
            System.out.println("Enviando mensaje de ocupación de clase: " + "Clase: "+clase.getNombre()+" - Ocupación: "+ocupacion);


            NotificacionDTO notificacionDTO = new NotificacionDTO();
            notificacionDTO.setMemberId(inscripcionDTO.getMiembroId());
            notificacionDTO.setEntrenadorId(clase.getEntrenadorID().getEntrenadorId());
            notificacionDTO.setMensaje("Inscrito a la clase "+clase.getNombre());

            System.out.println("Enviando notificación de inscripción: " + notificacionDTO.getMemberId());
            rabbitTemplate.convertAndSend("inscripcion.exchange", "inscripcion.routingkey", notificacionDTO);

            return "Miembro inscrito";

        } catch (ResponseStatusException e) {
            System.out.println("Error: "+e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(500));
        }

    }

    public Clase cambiarHorario(Long id, CambioHorarioDTO horario) {

        Clase clase = claseRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Clase no encontrada"));
        clase.setHorario(horario.getNuevoHorario());

        horario.setClaseId(id);
        claseRepository.save(clase);

        rabbitTemplate.convertAndSend("horario.exchange","", horario);

        System.out.println("Enviando notificación de cambio de horario: " + horario.getClaseId());

        return clase;
    }
}
