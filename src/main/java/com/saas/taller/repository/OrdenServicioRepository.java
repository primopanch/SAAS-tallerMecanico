package com.saas.taller.repository;

import com.saas.taller.entity.EstadoOrden;
import com.saas.taller.entity.OrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Long> {

    List<OrdenServicio> findByVehiculoId(Long vehiculoId);

    List<OrdenServicio> findByEstado(EstadoOrden estado);
}
