package srangeldev.camisapi.rest.pedidos.services;

import srangeldev.camisapi.rest.pedidos.models.Pedido;

import java.util.List;

public interface PedidoService {
    Pedido crearPedido(Pedido pedido);
    List<Pedido> listarPedidos();
    Pedido obtenerPorId(Long id);
    List<Pedido> obtenerPorUsuario(Long userId);
    List<Pedido> obtenerPorEstado(String estado);
    Pedido actualizarEstado(Long id, String nuevoEstado);
    void eliminarPedido(Long id);
}
