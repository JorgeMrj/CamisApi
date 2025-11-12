package srangeldev.camisapi.rest.carrito.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
     * Lista actualizada de IDs de productos
     */
    @NotNull(message = "La lista de items no puede ser nula")
    private List<@NotEmpty(message = "El ID del producto no puede estar vacío")
                 String> items;

    /**
     * Acción específica para realizar en el carrito
     * Útil para operaciones como agregar/quitar un item específico
     */
    private String accion; // "AGREGAR", "QUITAR", "LIMPIAR", "REEMPLAZAR"

    /**
     * ID de producto específico para acciones individuales
     */
   @NotNull(message = "El ID del producto no puede ser nulo")
    private String productoId;
}
