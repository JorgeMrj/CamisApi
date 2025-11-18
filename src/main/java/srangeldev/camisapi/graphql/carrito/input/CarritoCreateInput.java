package srangeldev.camisapi.graphql.carrito.input;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import srangeldev.camisapi.rest.productos.models.Producto;

import java.util.ArrayList;
import java.util.List;

/**
 * Input GraphQL simple para crear carrito (DAW)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoCreateInput {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @Builder.Default
    private List<Producto> productos = new ArrayList<>();
}
