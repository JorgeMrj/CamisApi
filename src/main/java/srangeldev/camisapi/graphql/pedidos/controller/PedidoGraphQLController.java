package srangeldev.camisapi.graphql.pedidos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import srangeldev.camisapi.graphql.pedidos.mapper.PedidoGraphQLMapper;
import srangeldev.camisapi.graphql.pedidos.output.PedidoGraphQLResponse;
import srangeldev.camisapi.rest.pedidos.models.EstadoPedido;
import srangeldev.camisapi.rest.pedidos.services.PedidoService;

import java.util.List;


@Controller
public class PedidoGraphQLController {

    private final PedidoService pedidoService;
    private final PedidoGraphQLMapper mapper;

    @Autowired
    public PedidoGraphQLController(PedidoService pedidoService, PedidoGraphQLMapper mapper) {
        this.pedidoService = pedidoService;
        this.mapper = mapper;
    }
    @QueryMapping
    public List<PedidoGraphQLResponse> pedidos() {
        var pedidos = pedidoService.listarPedidos();
        return mapper.toGraphQLResponseList(pedidos);
    }

    @QueryMapping
    public PedidoGraphQLResponse pedido(@Argument String id) {
        Long pedidoId = Long.parseLong(id);
        var pedido = pedidoService.obtenerPorId(pedidoId);
        return mapper.toGraphQLResponse(pedido);
    }

    @QueryMapping
    public List<PedidoGraphQLResponse> pedidosPorUsuario(@Argument String usuario) {
        var pedidos = pedidoService.findByUsuario(usuario);
        return mapper.toGraphQLResponseList(pedidos);
    }

    @QueryMapping
    public List<PedidoGraphQLResponse> pedidosPorEstado(@Argument String estado) {
        EstadoPedido estadoEnum = EstadoPedido.valueOf(estado.toUpperCase());
        var pedidos = pedidoService.buscarPorEstado(estadoEnum);
        return mapper.toGraphQLResponseList(pedidos);
    }
}
