package srangeldev.camisapi.rest.pedidos.services;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srangeldev.camisapi.rest.pedidos.dto.PedidoRequestDto;
import srangeldev.camisapi.rest.pedidos.dto.PedidoResponseDto;
import srangeldev.camisapi.rest.pedidos.exceptions.PedidoConflictException;
import srangeldev.camisapi.rest.pedidos.exceptions.PedidoNotFoundException;
import srangeldev.camisapi.rest.pedidos.mappers.PedidoMappers;
import srangeldev.camisapi.rest.pedidos.models.EstadoPedido;
import srangeldev.camisapi.rest.pedidos.models.Pedido;
import srangeldev.camisapi.rest.pedidos.repository.PedidoRepository;
import srangeldev.camisapi.rest.productos.models.EstadoProducto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Lógica de negocio
 *
 * Utiliza PedidoRepository para interactuar con la base de datos
 * y PedidoMappers para transformar entre entidades y DTOs.
 */
@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMappers pedidoMapper;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, PedidoMappers pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    // Creamos un nuevo pedido con estado PENDIENTE_PAGO
    @Override
    @Transactional //Asi si algo falla se revierte tod
    public PedidoResponseDto crearPedido(PedidoRequestDto pedidoRequest) {
        Pedido pedido = pedidoMapper.toPedido(pedidoRequest);
        pedido.setEstado(EstadoPedido.PENDIENTE_PAGO);
        pedido.setCreatedAt(LocalDateTime.now());
        Pedido saved = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDto(saved);
    }

    // Devuelve todos los pedidos
    @Override
    public List<PedidoResponseDto> listarPedidos() {
        return pedidoMapper.toResponseList(pedidoRepository.findAll());
    }

    // Obtiene los pedidos del usuario específico
    @Override
    public List<PedidoResponseDto> findByUsuario(Long userId) {
        List<Pedido> pedidos = pedidoRepository.findByUserId(userId);
        return pedidoMapper.toResponseList(pedidos);
    }

    // Buscamos el pedido por su Id
    @Override
    public PedidoResponseDto obtenerPorId(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido con ID " + pedidoId + " no encontrado"));
                return pedidoMapper.toResponseDto(pedido);
    }

    // Actualizamos el estado del pedido y registra fechas de pago y envío
    @Override
    public PedidoResponseDto actualizarEstado(Long pedidoId, EstadoPedido estado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido con ID " + pedidoId + " no encontrado"));

        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new PedidoConflictException("No es posible modificar un pedido cancelado");
        }
        if (!cambiosDeEstado(pedido.getEstado(), estado)) {
            throw new PedidoConflictException("No puedes cambiar del estado " + pedido.getEstado() + " a " + estado);
        }
        pedido.setEstado(estado);
        Pedido actualizado = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDto(actualizado);
    }
    //Posibles opcones en la actualizacion de los estados
    private boolean cambiosDeEstado(EstadoPedido estadoActual, EstadoPedido nuevoEstado) {
                return switch (estadoActual){
                    case PENDIENTE_PAGO -> nuevoEstado == EstadoPedido.PAGADO || nuevoEstado == EstadoPedido.CANCELADO;
                    case PAGADO -> nuevoEstado == EstadoPedido.ENVIADO || nuevoEstado == EstadoPedido.CANCELADO;
                    case ENVIADO -> nuevoEstado == EstadoPedido.ENTREGADO;
                    default -> false;
                };
    }

    // Pedidos filtrados por estado
    @Override
    public List<PedidoResponseDto> buscarPorEstado(EstadoPedido estado) {
        return pedidoMapper.toResponseList(pedidoRepository.findByEstado(estado));
    }

    //Eliminamos el pedido correspondiente al Id
    @Override
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}
