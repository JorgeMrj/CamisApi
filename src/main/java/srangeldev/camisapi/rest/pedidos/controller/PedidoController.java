package srangeldev.camisapi.rest.pedidos.controller;

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
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /**
     * Crea un nuevo pedido
     */
    @PostMapping
    public ResponseEntity<PedidoResponseDto> crearPedido(@Valid @RequestBody PedidoRequestDto pedidoRequest) {
        PedidoResponseDto nuevoPedido = pedidoService.crearPedido(pedidoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
    }

    /**
     * Obtiene todos los pedidos
     */
    @GetMapping
    public ResponseEntity<List<PedidoResponseDto>> listarPedidos() {
        List<PedidoResponseDto> pedidos = pedidoService.listarPedidos();
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Obtiene un pedido por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> obtenerPedido(@PathVariable Long id) {
        PedidoResponseDto pedido = pedidoService.obtenerPorId(id);
        return ResponseEntity.ok(pedido);
    }

    /**
     * Obtiene pedidos por el ID del usuario (String, MongoDB)
     */
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<PedidoResponseDto>> obtenerPedidosPorUsuario(@PathVariable Long userId) {
        List<PedidoResponseDto> pedidos = pedidoService.findByUsuario(userId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Obtiene pedidos por estado (ej: ?estado=PAGADO)
     */
    @GetMapping("/estado")
    public ResponseEntity<List<PedidoResponseDto>> obtenerPedidosPorEstado(@RequestParam EstadoPedido estado) {
        List<PedidoResponseDto> pedidos = pedidoService.buscarPorEstado(estado);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Actualiza el estado de un pedido
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponseDto> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPedido estado
    ) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }

    /**
     * Elimina un pedido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
