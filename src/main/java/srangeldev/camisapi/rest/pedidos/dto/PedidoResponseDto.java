package srangeldev.camisapi.rest.pedidos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import srangeldev.camisapi.rest.pedidos.models.EstadoPedido;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO PedidoResponseDTO
 *
 * Pedido completo para responder al cliente.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDto {

    private Long id;
    private String userId;
    private EstadoPedido estado;
    private LocalDateTime createdAt;
    private Double total;
    private LocalDateTime fechaPago;
    private LocalDateTime fechaEnvio;

    private List<DetallePedidoDto> detalles;



}
