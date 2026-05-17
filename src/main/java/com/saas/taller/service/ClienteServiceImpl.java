package com.saas.taller.service;

import com.saas.taller.dto.ClienteDTO;
import com.saas.taller.entity.Cliente;
import com.saas.taller.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        return mapToDTO(cliente);
    }

    @Override
    @Transactional
    public ClienteDTO create(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefono(clienteDTO.getTelefono());
        
        if (clienteDTO.getTipoEstado() != null) {
            cliente.setTipoEstado(clienteDTO.getTipoEstado());
        }

        // Asignar un número temporal único para cumplir con el NOT NULL UNIQUE de la BD
        cliente.setNumeroCliente("TMP-" + java.util.UUID.randomUUID().toString().substring(0, 8));

        // Guardar primero para obtener el ID, luego generar el numeroCliente (CLI-ID)
        Cliente savedCliente = clienteRepository.save(cliente);
        
        // Autogenerar numero_cliente definitivo
        String numeroGenerado = String.format("CLI-%04d", savedCliente.getId());
        savedCliente.setNumeroCliente(numeroGenerado);
        
        // Volver a guardar con el numeroCliente actualizado
        savedCliente = clienteRepository.save(savedCliente);
        
        return mapToDTO(savedCliente);
    }

    @Override
    @Transactional
    public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        cliente.setNombre(clienteDTO.getNombre());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefono(clienteDTO.getTelefono());
        
        if (clienteDTO.getTipoEstado() != null) {
            cliente.setTipoEstado(clienteDTO.getTipoEstado());
        }

        Cliente updatedCliente = clienteRepository.save(cliente);
        return mapToDTO(updatedCliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con id: " + id);
        }
        clienteRepository.deleteById(id);
    }

    // Método utilitario para mapear Entity a DTO
    private ClienteDTO mapToDTO(Cliente cliente) {
        return ClienteDTO.builder()
                .id(cliente.getId())
                .numeroCliente(cliente.getNumeroCliente())
                .nombre(cliente.getNombre())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .tipoEstado(cliente.getTipoEstado())
                .createdAt(cliente.getCreatedAt())
                .build();
    }
}
