package srangeldev.camisapi.graphql.carrito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import srangeldev.camisapi.graphql.carrito.input.CarritoCreateInput;
import srangeldev.camisapi.graphql.carrito.input.CarritoUpdateInput;
import srangeldev.camisapi.graphql.carrito.input.CarritoAddItemInput;
import srangeldev.camisapi.graphql.carrito.input.CarritoRemoveItemInput;
import srangeldev.camisapi.graphql.carrito.mapper.CarritoGraphQLMapper;
import srangeldev.camisapi.graphql.carrito.output.CarritoGraphQLResponse;
import srangeldev.camisapi.rest.carrito.dto.CarritoUpdateRequestDto;
import srangeldev.camisapi.rest.carrito.service.CarritoService;

import java.util.List;

/**
 * Controlador GraphQL para Carrito (versión simple para DAW)
 */
@Controller
public class CarritoGraphQLController {

    private final CarritoService carritoService;
    private final CarritoGraphQLMapper mapper;

    @Autowired
    public CarritoGraphQLController(CarritoService carritoService, CarritoGraphQLMapper mapper) {
        this.carritoService = carritoService;
        this.mapper = mapper;
    }

    // QUERIES (consultas)

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

    // MUTATIONS (operaciones de escritura)

    @MutationMapping
    public CarritoGraphQLResponse crearCarrito(@Argument CarritoCreateInput input) {
        var createDto = mapper.toCreateRequestDto(input);
        var carrito = carritoService.save(createDto);
        return mapper.toGraphQLResponse(carrito);
    }

    @MutationMapping
    public CarritoGraphQLResponse actualizarCarrito(@Argument String id, @Argument CarritoUpdateInput input) {
        Long carritoId = Long.parseLong(id);
        var updateDto = mapper.toUpdateRequestDto(input);
        var carrito = carritoService.update(carritoId, updateDto);
        return mapper.toGraphQLResponse(carrito);
    }

    @MutationMapping
    public CarritoGraphQLResponse eliminarCarrito(@Argument String id) {
        Long carritoId = Long.parseLong(id);
        var carrito = carritoService.delete(carritoId);
        return mapper.toGraphQLResponse(carrito);
    }

    @MutationMapping
    public CarritoGraphQLResponse agregarItemCarrito(@Argument CarritoAddItemInput input) {
        // Obtenemos el carrito actual
        var carritoActual = carritoService.getById(input.getCarritoId());

        // Agregamos el item a la lista
        carritoActual.getItems().add(input.getItemId());

        // Creamos el DTO de actualización
        var updateDto = CarritoUpdateRequestDto.builder()
                .id(input.getCarritoId())
                .items(carritoActual.getItems())
                .accion("AGREGAR")
                .productoId(input.getItemId())
                .build();

        var carrito = carritoService.update(input.getCarritoId(), updateDto);
        return mapper.toGraphQLResponse(carrito);
    }

    @MutationMapping
    public CarritoGraphQLResponse removerItemCarrito(@Argument CarritoRemoveItemInput input) {
        // Obtenemos el carrito actual
        var carritoActual = carritoService.getById(input.getCarritoId());

        // Removemos el item de la lista
        carritoActual.getItems().remove(input.getItemId());

        // Creamos el DTO de actualización
        var updateDto = CarritoUpdateRequestDto.builder()
                .id(input.getCarritoId())
                .items(carritoActual.getItems())
                .accion("QUITAR")
                .productoId(input.getItemId())
                .build();

        var carrito = carritoService.update(input.getCarritoId(), updateDto);
        return mapper.toGraphQLResponse(carrito);
    }

    @MutationMapping
    public CarritoGraphQLResponse vaciarCarrito(@Argument String id) {
        Long carritoId = Long.parseLong(id);

        // Creamos el DTO para vaciar el carrito
        var updateDto = CarritoUpdateRequestDto.builder()
                .id(carritoId)
                .items(List.of()) // Lista vacía
                .accion("LIMPIAR")
                .build();

        var carrito = carritoService.update(carritoId, updateDto);
        return mapper.toGraphQLResponse(carrito);
    }
}
