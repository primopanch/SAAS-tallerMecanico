package com.saas.taller.service;

import com.saas.taller.dto.OrdenServicioDTO;
import com.saas.taller.entity.EstadoOrden;
import com.saas.taller.entity.OrdenServicio;
import com.saas.taller.entity.Vehiculo;
import com.saas.taller.repository.OrdenServicioRepository;
import com.saas.taller.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdenServicioServiceImpl implements OrdenServicioService {

    private final OrdenServicioRepository ordenRepository;
    private final VehiculoRepository vehiculoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OrdenServicioDTO> findAll() {
        return ordenRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenServicioDTO> findByVehiculoId(Long vehiculoId) {
        if (!vehiculoRepository.existsById(vehiculoId)) {
            throw new RuntimeException("Vehículo no encontrado con id: " + vehiculoId);
        }
        return ordenRepository.findByVehiculoId(vehiculoId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenServicioDTO> findByEstado(EstadoOrden estado) {
        return ordenRepository.findByEstado(estado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenServicioDTO findById(Long id) {
        OrdenServicio orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con id: " + id));
        return mapToDTO(orden);
    }

    @Override
    @Transactional
    public OrdenServicioDTO create(OrdenServicioDTO ordenDTO) {
        Vehiculo vehiculo = vehiculoRepository.findById(ordenDTO.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + ordenDTO.getVehiculoId()));

        OrdenServicio orden = new OrdenServicio();
        orden.setVehiculo(vehiculo);
        orden.setReporteCliente(ordenDTO.getReporteCliente());
        orden.setDiagnosticoMecanico(ordenDTO.getDiagnosticoMecanico());

        if (ordenDTO.getEstado() != null) {
            orden.setEstado(ordenDTO.getEstado());
        }

        // UUID temporal para saltar la validacion NOT NULL UNIQUE
        orden.setIdInterno("TMP-ORD-" + java.util.UUID.randomUUID().toString().substring(0, 8));

        OrdenServicio savedOrden = ordenRepository.save(orden);

        // Autogenerar id_interno (Ej: ORD-2026-0001)
        int year = LocalDate.now().getYear();
        String idGenerado = String.format("ORD-%d-%04d", year, savedOrden.getId());
        savedOrden.setIdInterno(idGenerado);

        savedOrden = ordenRepository.save(savedOrden);
        return mapToDTO(savedOrden);
    }

    @Override
    @Transactional
    public OrdenServicioDTO update(Long id, OrdenServicioDTO ordenDTO) {
        OrdenServicio orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con id: " + id));

        orden.setReporteCliente(ordenDTO.getReporteCliente());
        orden.setDiagnosticoMecanico(ordenDTO.getDiagnosticoMecanico());

        if (ordenDTO.getEstado() != null) {
            orden.setEstado(ordenDTO.getEstado());
        }

        OrdenServicio updatedOrden = ordenRepository.save(orden);
        return mapToDTO(updatedOrden);
    }

    @Override
    @Transactional
    public OrdenServicioDTO updateEstado(Long id, EstadoOrden nuevoEstado) {
        OrdenServicio orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con id: " + id));

        orden.setEstado(nuevoEstado);
        OrdenServicio updatedOrden = ordenRepository.save(orden);
        return mapToDTO(updatedOrden);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!ordenRepository.existsById(id)) {
            throw new RuntimeException("Orden no encontrada con id: " + id);
        }
        ordenRepository.deleteById(id);
    }

    private OrdenServicioDTO mapToDTO(OrdenServicio orden) {
        return OrdenServicioDTO.builder()
                .id(orden.getId())
                .vehiculoId(orden.getVehiculo().getId())
                .idInterno(orden.getIdInterno())
                .reporteCliente(orden.getReporteCliente())
                .diagnosticoMecanico(orden.getDiagnosticoMecanico())
                .estado(orden.getEstado())
                .fechaCreacion(orden.getFechaCreacion())
                .build();
    }
}
