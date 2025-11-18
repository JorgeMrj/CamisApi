package srangeldev.camisapi.graphql.pedidos.mapper;

import org.springframework.stereotype.Component;
import srangeldev.camisapi.graphql.pedidos.output.PedidoGraphQLResponse;
import srangeldev.camisapi.rest.pedidos.dto.PedidoResponseDto;

import java.util.List;


@Component
public class PedidoGraphQLMapper {

    public PedidoGraphQLResponse toGraphQLResponse(PedidoResponseDto dto) {
        return PedidoGraphQLResponse.builder()
                .id(dto.getId() != null ? dto.getId().toString() : null)
                .usuario(dto.getUserId())
                .fechaPedido(dto.getCreatedAt() != null ? dto.getCreatedAt().toString() : null)
                .total(dto.getTotal())
                .estado(dto.getEstado() != null ? dto.getEstado().toString() : null)
                .detalles(dto.getDetalles() != null ?
                    dto.getDetalles().stream().map(Object::toString).toList() : null)
                .fechaPago(dto.getFechaPago() != null ? dto.getFechaPago().toString() : null)
                .fechaEnvio(dto.getFechaEnvio() != null ? dto.getFechaEnvio().toString() : null)
                .build();
    }

    public List<PedidoGraphQLResponse> toGraphQLResponseList(List<PedidoResponseDto> dtos) {
        return dtos.stream()
                .map(this::toGraphQLResponse)
                .toList();
    }
}
