package com.saas.taller.service;

import com.saas.taller.dto.VehiculoDTO;

import java.util.List;

public interface VehiculoService {

    List<VehiculoDTO> findAll();

    List<VehiculoDTO> findByClienteId(Long clienteId);

    VehiculoDTO findById(Long id);

    VehiculoDTO create(VehiculoDTO vehiculoDTO);

    VehiculoDTO update(Long id, VehiculoDTO vehiculoDTO);

    void delete(Long id);
}
