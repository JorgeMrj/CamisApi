package srangeldev.camisapi.rest.carrito.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para crear un nuevo carrito
 * Contiene la información mínima necesaria para la creación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoCreateRequestDto {

    /**
     * ID del usuario propietario del carrito
     * Obligatorio para asociar el carrito a un usuario específico
     */
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    /**
     * Lista inicial de IDs de productos (referencias a MongoDB)
     * Puede estar vacía al crear el carrito
     */
    @Builder.Default
    private List<String> productosIds = new ArrayList<>();
}
