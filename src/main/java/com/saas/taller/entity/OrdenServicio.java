package com.saas.taller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes_servicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "id_interno", nullable = false, unique = true, length = 50)
    private String idInterno;

    @Column(name = "reporte_cliente", columnDefinition = "TEXT")
    private String reporteCliente;

    @Column(name = "diagnostico_mecanico", columnDefinition = "TEXT")
    private String diagnosticoMecanico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        if (estado == null) {
            estado = EstadoOrden.EN_ESPERA;
        }
    }
}
