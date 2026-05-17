package com.saas.taller.repository;

import com.saas.taller.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByNumeroCliente(String numeroCliente);

    boolean existsByNumeroCliente(String numeroCliente);
}
