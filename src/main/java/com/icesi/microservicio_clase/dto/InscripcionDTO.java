package com.icesi.microservicio_clase.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class InscripcionDTO {
    @NotNull
    private Long miembroId;
}
