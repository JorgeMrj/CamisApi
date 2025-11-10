package srangeldev.camisapi.rest.pedidos.repository;

import srangeldev.camisapi.rest.pedidos.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{

    List<Pedido> findByUserId(Long userId);

}
