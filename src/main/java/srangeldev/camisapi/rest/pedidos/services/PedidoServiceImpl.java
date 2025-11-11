package srangeldev.camisapi.rest.pedidos.services;

import org.springframework.stereotype.Service;
import srangeldev.camisapi.rest.pedidos.models.EstadoPedido;
import srangeldev.camisapi.rest.pedidos.models.Pedido;
import srangeldev.camisapi.rest.pedidos.repository.PedidoRepository;
import srangeldev.camisapi.rest.pedidos.repository.PedidoRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoRepositoryImpl pedidoRepositoryImpl;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, PedidoRepositoryImpl pedidoRepositoryImpl) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoRepositoryImpl = pedidoRepositoryImpl;
    }

    @Override
    public Pedido crearPedido(Pedido pedido) {
        pedido.setEstado(EstadoPedido.PENDIENTE_PAGO);
        pedido.setFechaCreacion(LocalDateTime.now());
        return pedidoRepository.save(pedido);
    }

    @Override
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PEDIDO NO ENCONTRADO"));
    }

    @Override
    public List<Pedido> obtenerPorUsuario(Long userId) {
        return pedidoRepository.findByUserId(userId);
    }

    @Override
    public List<Pedido> obtenerPorEstado(EstadoPedido estado) {
        return pedidoRepositoryImpl.pedidosPorEstado(estado);
    }

    @Override
    public Pedido actualizarEstado(Long id, EstadoPedido estado) {
        Pedido pedido = obtenerPorId(id);
        pedido.setEstado(estado);
        if (estado == EstadoPedido.PAGADO) {
            pedido.setFechaPago(LocalDateTime.now());
        } else if (estado == EstadoPedido.ENVIADO) {
            pedido.setFechaEnvio(LocalDateTime.now());
        }
        return pedidoRepository.save(pedido);
    }

    @Override
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}
