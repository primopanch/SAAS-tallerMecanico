package com.saas.taller.dto;

import com.saas.taller.entity.TipoEstadoCliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private Long id;
    private String numeroCliente;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email(message = "Debe ser un formato de email válido")
    private String email;

    private String telefono;

    private TipoEstadoCliente tipoEstado;
    
    private LocalDateTime createdAt;
}
