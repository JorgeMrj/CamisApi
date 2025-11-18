package srangeldev.camisapi.graphql.productos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import srangeldev.camisapi.graphql.productos.mapper.ProductoGraphQLMapper;
import srangeldev.camisapi.graphql.productos.output.ProductoGraphQLResponse;
import srangeldev.camisapi.rest.productos.models.EstadoProducto;
import srangeldev.camisapi.rest.productos.service.ProductoService;

import java.util.List;


@Controller
public class ProductoGraphQLController {

    private final ProductoService productoService;
    private final ProductoGraphQLMapper mapper;

    @Autowired
    public ProductoGraphQLController(ProductoService productoService, ProductoGraphQLMapper mapper) {
        this.productoService = productoService;
        this.mapper = mapper;
    }

    @QueryMapping
    public List<ProductoGraphQLResponse> productos() {
        var productos = productoService.listarProductos();
        return mapper.toGraphQLResponseList(productos);
    }

    @QueryMapping
    public ProductoGraphQLResponse producto(@Argument String id) {
        var producto = productoService.obtenerPorId(id);
        return mapper.toGraphQLResponse(producto);
    }

    @QueryMapping
    public List<ProductoGraphQLResponse> productosPorNombre(@Argument String nombre) {
        var productos = productoService.buscarPorNombre(nombre);
        return mapper.toGraphQLResponseList(productos);
    }

    @QueryMapping
    public List<ProductoGraphQLResponse> productosPorEquipo(@Argument String equipo) {
        var productos = productoService.buscarPorEquipo(equipo);
        return mapper.toGraphQLResponseList(productos);
    }

    @QueryMapping
    public List<ProductoGraphQLResponse> productosPorEstado(@Argument String estado) {
        EstadoProducto estadoEnum = EstadoProducto.valueOf(estado.toUpperCase());
        var productos = productoService.buscarPorEstado(estadoEnum);
        return mapper.toGraphQLResponseList(productos);
    }

    @QueryMapping
    public List<ProductoGraphQLResponse> productosPorTalla(@Argument String talla) {
        var productos = productoService.buscarPorTalla(talla);
        return mapper.toGraphQLResponseList(productos);
    }
}
