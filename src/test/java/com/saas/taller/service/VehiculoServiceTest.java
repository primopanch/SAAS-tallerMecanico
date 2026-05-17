package com.saas.taller.service;

import com.saas.taller.dto.VehiculoDTO;
import com.saas.taller.entity.Cliente;
import com.saas.taller.entity.TipoEstadoCliente;
import com.saas.taller.entity.Vehiculo;
import com.saas.taller.repository.ClienteRepository;
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
class VehiculoServiceTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private VehiculoServiceImpl vehiculoService;

    private Cliente cliente;
    private VehiculoDTO vehiculoDTO;
    private Vehiculo vehiculo;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .id(1L)
                .numeroCliente("CLI-0001")
                .nombre("Empresa Logística SA")
                .tipoEstado(TipoEstadoCliente.CORPORATIVO)
                .build();

        vehiculoDTO = VehiculoDTO.builder()
                .clienteId(1L)
                .marca("Ford")
                .modelo("Transit")
                .anio(2022)
                .placas("ABC-123")
                .vin("1FDE123456789")
                .build();

        vehiculo = new Vehiculo();
        vehiculo.setId(10L);
        vehiculo.setCliente(cliente);
        vehiculo.setMarca("Ford");
        vehiculo.setModelo("Transit");
        vehiculo.setAnio(2022);
        vehiculo.setPlacas("ABC-123");
        vehiculo.setVin("1FDE123456789");
    }

    @Test
    void create_cuandoClienteExiste_debeGuardarVehiculo() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(vehiculoRepository.existsByVin("1FDE123456789")).thenReturn(false);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);

        VehiculoDTO result = vehiculoService.create(vehiculoDTO);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Ford", result.getMarca());
        verify(clienteRepository, times(1)).findById(1L);
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }

    @Test
    void create_cuandoClienteNoExiste_debeLanzarExcepcion() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            vehiculoService.create(vehiculoDTO);
        });

        assertEquals("Cliente no encontrado con id: 1", exception.getMessage());
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }
    
    @Test
    void create_cuandoVinYaExiste_debeLanzarExcepcion() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(vehiculoRepository.existsByVin("1FDE123456789")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            vehiculoService.create(vehiculoDTO);
        });

        assertEquals("Ya existe un vehículo con el VIN: 1FDE123456789", exception.getMessage());
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }
}
