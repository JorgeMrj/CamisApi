package srangeldev.camisapi.rest.carrito.controller;


import ch.qos.logback.classic.Logger;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import srangeldev.camisapi.rest.carrito.dto.CarritoCreateRequestDto;
import srangeldev.camisapi.rest.carrito.dto.CarritoResponseDto;
import srangeldev.camisapi.rest.carrito.dto.CarritoUpdateRequestDto;
import srangeldev.camisapi.rest.carrito.service.CarritoServiceImpl;

import java.util.List;

@RestController
@RequestMapping({"/api/carritos/", "/api/carritos"})
public class    CarritoRestController {
    private final CarritoServiceImpl carritoService;
    private final Logger logger = (Logger) LoggerFactory.getLogger(CarritoRestController.class);
    @Autowired
    public CarritoRestController(CarritoServiceImpl carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping("")
    public ResponseEntity<List<CarritoResponseDto>> getAll() {
        logger.info("Obteniendo carrito");
        return ResponseEntity.ok(carritoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDto> getById(@PathVariable Long id) {
        logger.info("Obteniendo carrito con id: " + id);
        return ResponseEntity.ok(carritoService.getById(id));
    }


    @GetMapping("/usuario/{userId}")
    public ResponseEntity<CarritoResponseDto> getByUserId(@PathVariable Long userId) {
        logger.info("Obteniendo carrito del usuario con id: " + userId);
        return ResponseEntity.ok(carritoService.findByUserId(userId));
    }

    @PostMapping("")
    public ResponseEntity<CarritoResponseDto> save(@Valid @RequestBody CarritoCreateRequestDto carrito) {
        logger.info("Recibida petición POST para crear carrito: {}", carrito);
        try {
            CarritoResponseDto response = carritoService.save(carrito);
            logger.info("carrito creada exitosamente con id: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error al crear carrito: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarritoResponseDto> update(@PathVariable Long id, @Valid @RequestBody CarritoUpdateRequestDto carrito) {
        logger.info("Actualizando Carrito con id: " + id);
        return ResponseEntity.ok(carritoService.update(id, carrito));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CarritoResponseDto> delete(@PathVariable Long id) {
        logger.info("Borrando carrito con id: " + id);
        return ResponseEntity.ok(carritoService.delete(id));
    }
}
