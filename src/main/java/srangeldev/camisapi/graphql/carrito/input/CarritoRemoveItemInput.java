package srangeldev.camisapi.graphql.carrito.input;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Input GraphQL para remover item del carrito (DAW)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoRemoveItemInput {

    @NotNull(message = "El ID del carrito es obligatorio")
    private Long carritoId;

    @NotNull(message = "El ID del item es obligatorio")
    private String itemId;
}
