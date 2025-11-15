package srangeldev.camisapi.rest.pedidos.services;

import srangeldev.camisapi.rest.pedidos.dto.PedidoRequestDto;
import srangeldev.camisapi.rest.pedidos.dto.PedidoResponseDto;
import srangeldev.camisapi.rest.pedidos.models.EstadoPedido;
import srangeldev.camisapi.rest.pedidos.models.Pedido;

import java.util.List;
import java.util.Optional;
/**
 * Servicio PedidoService
 *
 * Define las operaciones de negocio para la gestión de pedidos.
 */
public interface PedidoService {

    /**
     * Crea un nuevo pedido.
     */
    PedidoResponseDto crearPedido(PedidoRequestDto pedidoRequest);

    /**
     * Obtiene todos los pedidos.
     */
    List<PedidoResponseDto> listarPedidos();

    /**
     * Obtiene los pedidos por ID de usuario.
     */
    List<PedidoResponseDto> obtenerPorUsuario(Long userId);

    /**
     * Obtiene un pedido por su ID.
     */
    PedidoResponseDto obtenerPorId(Long pedidoId);

    /**
     * Cambia el estado de un pedido (por ejemplo, de PENDIENTE_PAGO a PAGADO).
     */
    PedidoResponseDto actualizarEstado(Long id, EstadoPedido estado);

    /**
     * Busca pedidos por estado.
     */
    List<PedidoResponseDto> obtenerPorEstado(EstadoPedido estado);

    /**
     * Eliminar Pedido.
     */
    void eliminarPedido(Long id);
}
