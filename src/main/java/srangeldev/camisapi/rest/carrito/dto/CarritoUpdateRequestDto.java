package srangeldev.camisapi.rest.carrito.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para actualizar un carrito existente
 * Permite modificar los items del carrito
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoUpdateRequestDto {

    /**
     * ID del carrito a actualizar
     */
    @NotNull(message = "El ID del carrito no puede ser nulo")
    private Long id;

    /**
     * Lista actualizada de IDs de productos (referencias a MongoDB)
     */
    private List<@NotEmpty(message = "El ID del producto no puede estar vacío") String> productosIds;

    /**
     * Acción específica para realizar en el carrito
     * Útil para operaciones como agregar/quitar un item específico
     */
    private String accion; // "AGREGAR", "QUITAR", "LIMPIAR", "REEMPLAZAR"

    /**
     * ID de producto específico para acciones individuales
     */
    private String productoId;
}
