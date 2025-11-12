package srangeldev.camisapi.rest.pedidos.mappers;

import org.springframework.stereotype.Component;
import srangeldev.camisapi.rest.pedidos.dto.DetallePedidoDto;
import srangeldev.camisapi.rest.pedidos.dto.PedidoRequestDto;
import srangeldev.camisapi.rest.pedidos.dto.PedidoResponseDto;
import srangeldev.camisapi.rest.pedidos.models.DetallePedido;
import srangeldev.camisapi.rest.pedidos.models.Pedido;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMappers {

    // De ResponseDto a Pedido
    public Pedido toPedido(PedidoRequestDto dto) {
        return Pedido.builder()
                .userId(dto.getUserId())
                .total(dto.getTotal())
                .detalles(dto.getDetalles().stream()
                        .map(this::toDetallePedido)
                        .collect(Collectors.toList()))
                .build();
    }

    public DetallePedido toDetallePedido(DetallePedidoDto dto) {
        return DetallePedido.builder()
                .productoId(dto.getProductoId())
                .nombre(dto.getNombre())
                .talla(dto.getTalla())
                .equipo(dto.getEquipo())
                .precioPagado(dto.getPrecioPagado())
                .imageUrl(dto.getImageUrl())
                .build();
    }

    // 🧩 Entidad → DTO
    public PedidoResponseDto toResponseDto(Pedido pedido) {
        return new PedidoResponseDto(
                pedido.getId(),
                pedido.getUserId(),
                pedido.getEstado(),
                pedido.getCreatedAt(),
                pedido.getTotal(),
                pedido.getFechaPago(),
                pedido.getFechaEnvio(),
                pedido.getDetalles().stream()
                        .map(this::toDetallePedidoDto)
                        .toList()
        );
    }

    public DetallePedidoDto toDetallePedidoDto(DetallePedido entity) {
        return new DetallePedidoDto(
                entity.getProductoId(),
                entity.getNombre(),
                entity.getTalla(),
                entity.getEquipo(),
                entity.getPrecioPagado(),
                entity.getImageUrl()
        );
    }

    // 🧩 Lista de pedidos → lista de DTOs (tu estilo preferido)
    public List<PedidoResponseDto> toResponseList(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(p -> new PedidoResponseDto(
                        p.getId(),
                        p.getUserId(),
                        p.getEstado(),
                        p.getCreatedAt(),
                        p.getTotal(),
                        p.getFechaPago(),
                        p.getFechaEnvio(),
                        p.getDetalles().stream()
                                .map(this::toDetallePedidoDto)
                                .toList()
                ))
                .toList();
    }
}