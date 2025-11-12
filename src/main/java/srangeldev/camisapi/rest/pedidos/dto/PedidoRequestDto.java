package srangeldev.camisapi.rest.pedidos.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import srangeldev.camisapi.rest.pedidos.models.EstadoPedido;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO PedidoRequestDTO
 *
 * Utilizado para crear un nuevo pedido desde el cliente.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PedidoRequestDto {
    @NotNull(message = "El usuario no puede ser nulo")
    private String userId;

    @NotNull(message = "El total no puede ser nulo")
    @Positive(message = "El total debe ser mayor que 0")
    private Double total;

    @NotNull(message = "Debe incluir al menos un producto")
    private List<DetallePedidoDto> detalles;
}
