package srangeldev.camisapi.rest.pedidos.repository;

import srangeldev.camisapi.rest.pedidos.models.Pedido;

import java.util.List;

@Repository
public class PedidoRepositoryImpl {

    @PersistenceContext
    private EntityManager em;

    //Consulta pa obtener pedidos por estado
    public List<Pedido> buscarPedidosPorEstado(String estado) {
        String jpql = "SELECT p FROM Pedido p WHERE p.estado = :estado";
        TypedQuery<Pedido> query = em.createQuery(jpql, Pedido.class);
        query.setParameter("estado", estado);
        return query.getResultList();
    }

}
