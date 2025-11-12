package srangeldev.camisapi.rest.pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO DetallePedidoDto
 *
 * Representa los datos del producto en el momento de la venta.
 * Equivalente a la subentidad embebida DetallePedido.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDto {
    @NotNull(message = "El ID del producto no puede ser nulo")
    private Long productoId;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La talla no puede estar vacía")
    private String talla;

    @NotBlank(message = "El equipo no puede estar vacío")
    private String equipo;

    @NotNull(message = "El precio pagado no puede ser nulo")
    @Positive(message = "El precio pagado debe ser positivo")
    private Double precioPagado;

    private String imageUrl;
}
