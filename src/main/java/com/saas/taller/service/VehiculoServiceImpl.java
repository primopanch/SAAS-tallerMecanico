package com.saas.taller.service;

import com.saas.taller.dto.VehiculoDTO;
import com.saas.taller.entity.Cliente;
import com.saas.taller.entity.Vehiculo;
import com.saas.taller.repository.ClienteRepository;
import com.saas.taller.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoDTO> findAll() {
        return vehiculoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoDTO> findByClienteId(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new RuntimeException("Cliente no encontrado con id: " + clienteId);
        }
        return vehiculoRepository.findByClienteId(clienteId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculoDTO findById(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));
        return mapToDTO(vehiculo);
    }

    @Override
    @Transactional
    public VehiculoDTO create(VehiculoDTO vehiculoDTO) {
        Cliente cliente = clienteRepository.findById(vehiculoDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + vehiculoDTO.getClienteId()));

        if (vehiculoDTO.getVin() != null && vehiculoRepository.existsByVin(vehiculoDTO.getVin())) {
            throw new RuntimeException("Ya existe un vehículo con el VIN: " + vehiculoDTO.getVin());
        }

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setCliente(cliente);
        vehiculo.setMarca(vehiculoDTO.getMarca());
        vehiculo.setModelo(vehiculoDTO.getModelo());
        vehiculo.setAnio(vehiculoDTO.getAnio());
        vehiculo.setColor(vehiculoDTO.getColor());
        vehiculo.setPlacas(vehiculoDTO.getPlacas());
        vehiculo.setVin(vehiculoDTO.getVin());
        vehiculo.setNotaVisualUrl(vehiculoDTO.getNotaVisualUrl());

        Vehiculo savedVehiculo = vehiculoRepository.save(vehiculo);
        return mapToDTO(savedVehiculo);
    }

    @Override
    @Transactional
    public VehiculoDTO update(Long id, VehiculoDTO vehiculoDTO) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));

        // Si cambia el cliente (opcional, dependiendo de las reglas de negocio)
        if (!vehiculo.getCliente().getId().equals(vehiculoDTO.getClienteId())) {
            Cliente nuevoCliente = clienteRepository.findById(vehiculoDTO.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + vehiculoDTO.getClienteId()));
            vehiculo.setCliente(nuevoCliente);
        }

        vehiculo.setMarca(vehiculoDTO.getMarca());
        vehiculo.setModelo(vehiculoDTO.getModelo());
        vehiculo.setAnio(vehiculoDTO.getAnio());
        vehiculo.setColor(vehiculoDTO.getColor());
        vehiculo.setPlacas(vehiculoDTO.getPlacas());
        vehiculo.setVin(vehiculoDTO.getVin());
        vehiculo.setNotaVisualUrl(vehiculoDTO.getNotaVisualUrl());

        Vehiculo updatedVehiculo = vehiculoRepository.save(vehiculo);
        return mapToDTO(updatedVehiculo);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new RuntimeException("Vehículo no encontrado con id: " + id);
        }
        vehiculoRepository.deleteById(id);
    }

    private VehiculoDTO mapToDTO(Vehiculo vehiculo) {
        return VehiculoDTO.builder()
                .id(vehiculo.getId())
                .clienteId(vehiculo.getCliente().getId())
                .marca(vehiculo.getMarca())
                .modelo(vehiculo.getModelo())
                .anio(vehiculo.getAnio())
                .color(vehiculo.getColor())
                .placas(vehiculo.getPlacas())
                .vin(vehiculo.getVin())
                .notaVisualUrl(vehiculo.getNotaVisualUrl())
                .createdAt(vehiculo.getCreatedAt())
                .build();
    }
}
