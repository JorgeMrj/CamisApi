package srangeldev.camisapi.rest.pedidos.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import srangeldev.camisapi.rest.pedidos.models.EstadoPedido;
import srangeldev.camisapi.rest.pedidos.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{

    List<Pedido> findByUserId(Long userId);

    @Query("SELECT p FROM Pedido p WHERE p.estado = :estado")
    List<Pedido> pedidosPorEstado(EstadoPedido estado);

}
