package srangeldev.camisapi.rest.pedidos.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import srangeldev.camisapi.rest.pedidos.models.EstadoPedido;
import srangeldev.camisapi.rest.pedidos.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


/**
 * Repositorio JPA para la entidad Pedido.
 *
 * Gestiona las operaciones de CRUD basicas
 * Define consultas personalizadas relacionadas con los pedidos almacenados en PostgreSQL.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{

    /**
     * Busca todos los pedidos asociados a un usuario específico.
     *
     * @param userId identificador del usuario
     * @return lista de pedidos pertenecientes al usuario indicado
     */
    List<Pedido> findByUserId(Long userId);

    /**
     * Obtiene los pedidos filtrados por su estado actual.
     *
     * @param estado estado del pedido
     * @return lista de pedidos con el estado
     */
    @Query("SELECT p FROM Pedido p WHERE p.estado = :estado")
    List<Pedido> findByEstado(EstadoPedido estado);
}
