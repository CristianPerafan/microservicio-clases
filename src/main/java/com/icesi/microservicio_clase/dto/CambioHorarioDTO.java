package com.icesi.microservicio_clase.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class CambioHorarioDTO implements Serializable {
    @NotNull
    private LocalDateTime nuevoHorario;

    private Long claseId;
}
