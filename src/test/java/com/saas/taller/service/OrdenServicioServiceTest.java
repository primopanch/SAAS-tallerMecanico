package com.saas.taller.service;

import com.saas.taller.dto.OrdenServicioDTO;
import com.saas.taller.entity.EstadoOrden;
import com.saas.taller.entity.OrdenServicio;
import com.saas.taller.entity.Vehiculo;
import com.saas.taller.repository.OrdenServicioRepository;
import com.saas.taller.repository.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenServicioServiceTest {

    @Mock
    private OrdenServicioRepository ordenRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private OrdenServicioServiceImpl ordenService;

    private Vehiculo vehiculo;
    private OrdenServicioDTO ordenDTO;
    private OrdenServicio orden;

    @BeforeEach
    void setUp() {
        vehiculo = new Vehiculo();
        vehiculo.setId(10L);

        ordenDTO = OrdenServicioDTO.builder()
                .vehiculoId(10L)
                .reporteCliente("El motor hace ruido")
                .estado(EstadoOrden.EN_ESPERA)
                .build();

        orden = new OrdenServicio();
        orden.setId(5L);
        orden.setVehiculo(vehiculo);
        orden.setReporteCliente("El motor hace ruido");
        orden.setEstado(EstadoOrden.EN_ESPERA);
    }

    @Test
    void create_cuandoVehiculoExiste_debeGuardarYGenerarIdInterno() {
        when(vehiculoRepository.findById(10L)).thenReturn(Optional.of(vehiculo));
        when(ordenRepository.save(any(OrdenServicio.class))).thenReturn(orden);

        OrdenServicioDTO result = ordenService.create(ordenDTO);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("El motor hace ruido", result.getReporteCliente());
        verify(vehiculoRepository, times(1)).findById(10L);
        // Se llama dos veces, la primera genera el temporal, la segunda actualiza el ID final
        verify(ordenRepository, times(2)).save(any(OrdenServicio.class));
    }

    @Test
    void updateEstado_debeActualizarYRetornarOrden() {
        when(ordenRepository.findById(5L)).thenReturn(Optional.of(orden));
        
        OrdenServicio ordenModificada = new OrdenServicio();
        ordenModificada.setId(5L);
        ordenModificada.setVehiculo(vehiculo);
        ordenModificada.setEstado(EstadoOrden.REPARACION);

        when(ordenRepository.save(any(OrdenServicio.class))).thenReturn(ordenModificada);

        OrdenServicioDTO result = ordenService.updateEstado(5L, EstadoOrden.REPARACION);

        assertNotNull(result);
        assertEquals(EstadoOrden.REPARACION, result.getEstado());
        verify(ordenRepository, times(1)).save(any(OrdenServicio.class));
    }
}
