package com.saas.taller.controller;

import com.saas.taller.dto.OrdenServicioDTO;
import com.saas.taller.entity.EstadoOrden;
import com.saas.taller.service.OrdenServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenServicioController {

    private final OrdenServicioService ordenService;

    @GetMapping
    public ResponseEntity<List<OrdenServicioDTO>> getAllOrdenes() {
        return ResponseEntity.ok(ordenService.findAll());
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<OrdenServicioDTO>> getOrdenesByVehiculoId(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(ordenService.findByVehiculoId(vehiculoId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<OrdenServicioDTO>> getOrdenesByEstado(@PathVariable EstadoOrden estado) {
        return ResponseEntity.ok(ordenService.findByEstado(estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenServicioDTO> getOrdenById(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.findById(id));
    }

    @PostMapping
    public ResponseEntity<OrdenServicioDTO> createOrden(@Valid @RequestBody OrdenServicioDTO ordenDTO) {
        OrdenServicioDTO newOrden = ordenService.create(ordenDTO);
        return new ResponseEntity<>(newOrden, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenServicioDTO> updateOrden(@PathVariable Long id, @Valid @RequestBody OrdenServicioDTO ordenDTO) {
        return ResponseEntity.ok(ordenService.update(id, ordenDTO));
    }

    // Endpoint específico para cambiar el estado rápidamente
    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenServicioDTO> updateEstado(@PathVariable Long id, @RequestParam EstadoOrden nuevoEstado) {
        return ResponseEntity.ok(ordenService.updateEstado(id, nuevoEstado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrden(@PathVariable Long id) {
        ordenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
