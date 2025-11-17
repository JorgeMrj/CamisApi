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

    /**
     * Convertimos el PedidoRequestDto recibido a la entidad Pedido.
     *
     * @param dto el objeto con los datos del pedido provenientes de la solicitud.
     * @return una instancia de Pedido con los valores correspondientes.
     */
    public Pedido toPedido(PedidoRequestDto dto) {
        return Pedido.builder()
                .userId(dto.getUserId())
                .total(dto.getTotal())
                .detalles(dto.getDetalles().stream()
                        .map(this::toDetallePedido)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Convierte la entidad Pedido a PedidoResponseDto.
     * Incluye la lista de detalles del pedido transformada a DTOs.
     *
     * @param pedido la entidad del pedido obtenida desde la base de datos.
     * @return un objeto PedidoResponseDto con la información lista para ser serializada.
     */
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
    /**
     * Convierte  DetallePedidoDto a la entidad  DetallePedido.
     * Utilizado al crear un pedido nuevo a partir de los datos enviados por el usuario.
     *
     * @param dto el detalle del pedido en formato DTO.
     * @return una instancia de DetallePedido con sus datos correspondientes.
     */
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

    /**
     * Convierte la entidad DetallePedido aDetallePedidoDto.
     *
     * @param entity la entidad de detalle del pedido.
     * @return el DTO equivalente del detalle del pedido.
     */
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
    /**
     * Convierte una lista de entidades Pedido en una lista de PedidoResponseDto.
     * Para respuestas que devuelven múltiples pedidos.
     *
     * @param pedidos lista de entidades de Pedido.
     * @return lista de objetos PedidoResponseDto con los datos transformados.
     */
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