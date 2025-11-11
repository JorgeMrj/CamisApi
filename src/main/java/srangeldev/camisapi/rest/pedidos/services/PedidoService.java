package srangeldev.camisapi.rest.pedidos.services;

import srangeldev.camisapi.rest.pedidos.models.EstadoPedido;
import srangeldev.camisapi.rest.pedidos.models.Pedido;

import java.util.List;

public interface PedidoService {

    Pedido crearPedido(Pedido pedido);
    List<Pedido> listarPedidos();
    Pedido obtenerPorId(Long id);
    List<Pedido> obtenerPorUsuario(Long userId);
    List<Pedido> obtenerPorEstado(EstadoPedido estado);
    Pedido actualizarEstado(Long id, EstadoPedido estado);
    void eliminarPedido(Long id);

}
