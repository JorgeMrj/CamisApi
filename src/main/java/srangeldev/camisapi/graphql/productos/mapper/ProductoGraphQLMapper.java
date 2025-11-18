package srangeldev.camisapi.graphql.productos.mapper;

import org.springframework.stereotype.Component;
import srangeldev.camisapi.graphql.productos.output.ProductoGraphQLResponse;
import srangeldev.camisapi.rest.productos.dto.ProductoResponseDTO;

import java.util.List;


@Component
public class ProductoGraphQLMapper {

    public ProductoGraphQLResponse toGraphQLResponse(ProductoResponseDTO dto) {
        return ProductoGraphQLResponse.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .equipo(dto.getEquipo())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .imageUrl(dto.getImageUrl())
                .estado(dto.getEstado() != null ? dto.getEstado().toString() : null)
                .talla(dto.getTalla())
                .fechaCreacion(dto.getFechaCreacion() != null ? dto.getFechaCreacion().toString() : null)
                .build();
    }

    public List<ProductoGraphQLResponse> toGraphQLResponseList(List<ProductoResponseDTO> dtos) {
        return dtos.stream()
                .map(this::toGraphQLResponse)
                .toList();
    }
}
