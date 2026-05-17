package com.saas.taller.service;

import com.saas.taller.dto.ClienteDTO;
import com.saas.taller.entity.Cliente;
import com.saas.taller.entity.TipoEstadoCliente;
import com.saas.taller.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente1;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        cliente1 = Cliente.builder()
                .id(1L)
                .numeroCliente("CLI-0001")
                .nombre("Juan Perez")
                .email("juan@ejemplo.com")
                .telefono("555-1234")
                .tipoEstado(TipoEstadoCliente.ACTIVO)
                .createdAt(LocalDateTime.now())
                .build();

        clienteDTO = ClienteDTO.builder()
                .nombre("Juan Perez")
                .email("juan@ejemplo.com")
                .telefono("555-1234")
                .tipoEstado(TipoEstadoCliente.ACTIVO)
                .build();
    }

    @Test
    void findAll_debeRetornarListaDeClientes() {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1));

        List<ClienteDTO> result = clienteService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Juan Perez", result.get(0).getNombre());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void findById_cuandoExiste_debeRetornarCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente1));

        ClienteDTO result = clienteService.findById(1L);

        assertNotNull(result);
        assertEquals("Juan Perez", result.getNombre());
        assertEquals("CLI-0001", result.getNumeroCliente());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void findById_cuandoNoExiste_debeLanzarExcepcion() {
        when(clienteRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            clienteService.findById(2L);
        });

        assertEquals("Cliente no encontrado con id: 2", exception.getMessage());
        verify(clienteRepository, times(1)).findById(2L);
    }

    @Test
    void create_debeGuardarYRetornarCliente() {
        // Al crear, se guarda dos veces. Primero para obtener el ID, luego para setear el CLI-ID.
        Cliente clienteConId = Cliente.builder()
                .id(1L)
                .nombre(clienteDTO.getNombre())
                .tipoEstado(clienteDTO.getTipoEstado())
                .build();

        Cliente clienteFinal = Cliente.builder()
                .id(1L)
                .numeroCliente("CLI-0001")
                .nombre(clienteDTO.getNombre())
                .tipoEstado(clienteDTO.getTipoEstado())
                .build();

        // El primer save devuelve clienteConId, el segundo devuelve clienteFinal
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteConId).thenReturn(clienteFinal);

        ClienteDTO result = clienteService.create(clienteDTO);

        assertNotNull(result);
        assertEquals("CLI-0001", result.getNumeroCliente());
        assertEquals("Juan Perez", result.getNombre());
        verify(clienteRepository, times(2)).save(any(Cliente.class));
    }
}
