package com.saas.taller.service;

import com.saas.taller.dto.ClienteDTO;

import java.util.List;

public interface ClienteService {

    List<ClienteDTO> findAll();

    ClienteDTO findById(Long id);

    ClienteDTO create(ClienteDTO clienteDTO);

    ClienteDTO update(Long id, ClienteDTO clienteDTO);

    void delete(Long id);
}
