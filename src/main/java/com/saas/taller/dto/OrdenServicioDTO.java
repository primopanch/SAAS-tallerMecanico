package com.saas.taller.dto;

import com.saas.taller.entity.EstadoOrden;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenServicioDTO {

    private Long id;

    @NotNull(message = "El ID del vehículo es obligatorio")
    private Long vehiculoId;

    private String idInterno;

    private String reporteCliente;

    private String diagnosticoMecanico;

    private EstadoOrden estado;

    private LocalDateTime fechaCreacion;
}
