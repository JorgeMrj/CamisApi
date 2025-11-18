package srangeldev.camisapi.rest.pedidos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import srangeldev.camisapi.rest.pedidos.dto.PedidoRequestDto;
import srangeldev.camisapi.rest.pedidos.dto.PedidoResponseDto;
import srangeldev.camisapi.rest.pedidos.models.EstadoPedido;
import srangeldev.camisapi.rest.pedidos.services.PedidoService;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "API para gestión de pedidos de compra")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(
        summary = "Crear nuevo pedido",
        description = "Crea un nuevo pedido con los datos proporcionados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Pedido creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PedidoResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        )
    })
    @PostMapping
    public ResponseEntity<PedidoResponseDto> crearPedido(
        @Parameter(description = "Datos del pedido a crear", required = true)
        @Valid @RequestBody PedidoRequestDto pedidoRequest) {
        PedidoResponseDto nuevoPedido = pedidoService.crearPedido(pedidoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
    }

    @Operation(
        summary = "Obtener todos los pedidos",
        description = "Recupera una lista completa de todos los pedidos"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de pedidos recuperada exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = PedidoResponseDto.class))
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<PedidoResponseDto>> listarPedidos() {
        List<PedidoResponseDto> pedidos = pedidoService.listarPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @Operation(
        summary = "Obtener pedido por ID",
        description = "Recupera un pedido específico mediante su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Pedido encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PedidoResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pedido no encontrado"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> obtenerPedido(
        @Parameter(description = "ID único del pedido", required = true) @PathVariable Long id) {
        PedidoResponseDto pedido = pedidoService.obtenerPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @Operation(
        summary = "Obtener pedidos por usuario",
        description = "Recupera todos los pedidos asociados a un usuario específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de pedidos del usuario recuperada exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = PedidoResponseDto.class))
            )
        )
    })
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<PedidoResponseDto>> obtenerPedidosPorUsuario(
        @Parameter(description = "ID del usuario (MongoDB)", required = true) @PathVariable String userId) {
        List<PedidoResponseDto> pedidos = pedidoService.findByUsuario(userId);
        return ResponseEntity.ok(pedidos);
    }

    @Operation(
        summary = "Obtener pedidos por estado",
        description = "Filtra pedidos según su estado (PENDIENTE, PAGADO, ENVIADO, ENTREGADO, CANCELADO)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de pedidos filtrada por estado",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = PedidoResponseDto.class))
            )
        )
    })
    @GetMapping("/estado")
    public ResponseEntity<List<PedidoResponseDto>> obtenerPedidosPorEstado(
        @Parameter(description = "Estado del pedido", required = true) @RequestParam EstadoPedido estado) {
        List<PedidoResponseDto> pedidos = pedidoService.buscarPorEstado(estado);
        return ResponseEntity.ok(pedidos);
    }

    @Operation(
        summary = "Actualizar estado del pedido",
        description = "Actualiza únicamente el estado de un pedido específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Estado del pedido actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PedidoResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pedido no encontrado"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Estado inválido"
        )
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponseDto> actualizarEstado(
        @Parameter(description = "ID del pedido", required = true) @PathVariable Long id,
        @Parameter(description = "Nuevo estado del pedido", required = true) @Valid @RequestParam EstadoPedido estado
    ) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }

    @Operation(
        summary = "Eliminar pedido",
        description = "Elimina un pedido específico del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Pedido eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pedido no encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(
        @Parameter(description = "ID del pedido a eliminar", required = true) @PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
