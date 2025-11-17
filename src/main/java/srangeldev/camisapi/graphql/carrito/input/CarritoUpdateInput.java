package srangeldev.camisapi.graphql.carrito.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Input GraphQL simple para actualizar carrito (DAW)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoUpdateInput {

    private List<String> items;
}
