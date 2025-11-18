package srangeldev.camisapi.rest.carrito.controller;


import ch.qos.logback.classic.Logger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Carrito", description = "API para gestión de carritos de compra")
public class CarritoRestController {
    private final CarritoServiceImpl carritoService;
    private final Logger logger = (Logger) LoggerFactory.getLogger(CarritoRestController.class);

    @Autowired
    public CarritoRestController(CarritoServiceImpl carritoService) {
        this.carritoService = carritoService;
    }

    @Operation(
        summary = "Obtener todos los carritos",
        description = "Recupera una lista de todos los carritos existentes"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de carritos recuperada exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = CarritoResponseDto.class))
            )
        )
    })
    @GetMapping("")
    public ResponseEntity<List<CarritoResponseDto>> getAll() {
        logger.info("Obteniendo carrito");
        return ResponseEntity.ok(carritoService.getAll());
    }

    @Operation(
        summary = "Obtener carrito por ID",
        description = "Recupera un carrito específico mediante su identificador"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Carrito encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CarritoResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Carrito no encontrado"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDto> getById(
        @Parameter(description = "ID del carrito", required = true) @PathVariable Long id) {
        logger.info("Obteniendo carrito con id: " + id);
        return ResponseEntity.ok(carritoService.getById(id));
    }

    @Operation(
        summary = "Obtener carrito por ID de usuario",
        description = "Recupera el carrito asociado a un usuario específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Carrito del usuario encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CarritoResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Carrito del usuario no encontrado"
        )
    })
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<CarritoResponseDto> getByUserId(
        @Parameter(description = "ID del usuario", required = true) @PathVariable Long userId) {
        logger.info("Obteniendo carrito del usuario con id: " + userId);
        return ResponseEntity.ok(carritoService.findByUserId(userId));
    }

    @Operation(
        summary = "Crear nuevo carrito",
        description = "Crea un nuevo carrito con los datos proporcionados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Carrito creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CarritoResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        )
    })
    @PostMapping("")
    public ResponseEntity<CarritoResponseDto> save(
        @Parameter(description = "Datos del carrito a crear", required = true)
        @Valid @RequestBody CarritoCreateRequestDto carrito) {
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

    @Operation(
        summary = "Actualizar carrito",
        description = "Actualiza un carrito existente con los nuevos datos proporcionados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Carrito actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CarritoResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Carrito no encontrado"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<CarritoResponseDto> update(
        @Parameter(description = "ID del carrito", required = true) @PathVariable Long id,
        @Parameter(description = "Datos del carrito a actualizar", required = true)
        @Valid @RequestBody CarritoUpdateRequestDto carrito) {
        logger.info("Actualizando Carrito con id: " + id);
        return ResponseEntity.ok(carritoService.update(id, carrito));
    }

    @Operation(
        summary = "Eliminar carrito",
        description = "Elimina un carrito específico mediante su identificador"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Carrito eliminado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CarritoResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Carrito no encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CarritoResponseDto> delete(
        @Parameter(description = "ID del carrito", required = true) @PathVariable Long id) {
        logger.info("Borrando carrito con id: " + id);
        return ResponseEntity.ok(carritoService.delete(id));
    }
}
