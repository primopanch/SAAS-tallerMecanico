package com.saas.taller.service;

import com.saas.taller.dto.OrdenServicioDTO;
import com.saas.taller.entity.EstadoOrden;

import java.util.List;

public interface OrdenServicioService {

    List<OrdenServicioDTO> findAll();

    List<OrdenServicioDTO> findByVehiculoId(Long vehiculoId);

    List<OrdenServicioDTO> findByEstado(EstadoOrden estado);

    OrdenServicioDTO findById(Long id);

    OrdenServicioDTO create(OrdenServicioDTO ordenDTO);

    OrdenServicioDTO update(Long id, OrdenServicioDTO ordenDTO);

    OrdenServicioDTO updateEstado(Long id, EstadoOrden nuevoEstado);

    void delete(Long id);
}
