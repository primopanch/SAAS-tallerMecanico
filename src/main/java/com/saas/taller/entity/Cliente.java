package com.saas.taller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cliente", nullable = false, unique = true, length = 20)
    private String numeroCliente;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_estado")
    private TipoEstadoCliente tipoEstado;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (tipoEstado == null) {
            tipoEstado = TipoEstadoCliente.ACTIVO;
        }
    }
}
