package com.saas.taller.controller;

import com.saas.taller.dto.VehiculoDTO;
import com.saas.taller.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @GetMapping
    public ResponseEntity<List<VehiculoDTO>> getAllVehiculos() {
        return ResponseEntity.ok(vehiculoService.findAll());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(vehiculoService.findByClienteId(clienteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculoDTO> getVehiculoById(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<VehiculoDTO> createVehiculo(@Valid @RequestBody VehiculoDTO vehiculoDTO) {
        VehiculoDTO newVehiculo = vehiculoService.create(vehiculoDTO);
        return new ResponseEntity<>(newVehiculo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoDTO> updateVehiculo(@PathVariable Long id, @Valid @RequestBody VehiculoDTO vehiculoDTO) {
        return ResponseEntity.ok(vehiculoService.update(id, vehiculoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehiculo(@PathVariable Long id) {
        vehiculoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
