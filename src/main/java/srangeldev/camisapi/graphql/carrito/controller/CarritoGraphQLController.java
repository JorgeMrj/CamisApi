package srangeldev.camisapi.graphql.carrito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import srangeldev.camisapi.graphql.carrito.mapper.CarritoGraphQLMapper;
import srangeldev.camisapi.graphql.carrito.output.CarritoGraphQLResponse;
import srangeldev.camisapi.rest.carrito.service.CarritoService;

import java.util.List;


@Controller
public class CarritoGraphQLController {

    private final CarritoService carritoService;
    private final CarritoGraphQLMapper mapper;

    @Autowired
    public CarritoGraphQLController(CarritoService carritoService, CarritoGraphQLMapper mapper) {
        this.carritoService = carritoService;
        this.mapper = mapper;
    }

    @QueryMapping
    public List<CarritoGraphQLResponse> carritos() {
        var carritos = carritoService.getAll();
        return mapper.toGraphQLResponseList(carritos);
    }

    @QueryMapping
    public CarritoGraphQLResponse carrito(@Argument String id) {
        Long carritoId = Long.parseLong(id);
        var carrito = carritoService.getById(carritoId);
        return mapper.toGraphQLResponse(carrito);
    }

    @QueryMapping
    public CarritoGraphQLResponse carritoByUserId(@Argument String userId) {
        Long userIdLong = Long.parseLong(userId);
        var carrito = carritoService.findByUserId(userIdLong);
        return mapper.toGraphQLResponse(carrito);
    }
}
