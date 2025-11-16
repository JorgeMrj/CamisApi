package srangeldev.camisapi.rest.productos.mapper;

import org.springframework.stereotype.Component;
import srangeldev.camisapi.rest.productos.dto.ProductoRequestDTO;
import srangeldev.camisapi.rest.productos.dto.ProductoResponseDTO;
import srangeldev.camisapi.rest.productos.models.Producto;

@Component
public class ProductoMapper {

    // De DTO de request a entidad
    public Producto toEntity(ProductoRequestDTO dto) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .equipo(dto.getEquipo())
                .talla(dto.getTalla())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .imageUrl(dto.getImageUrl())
                .estado(dto.getEstado())
                .build();
    }

    // De entidad a DTO de response
    public ProductoResponseDTO toDTO(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .equipo(producto.getEquipo())
                .talla(producto.getTalla())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .imageUrl(producto.getImageUrl())
                .estado(producto.getEstado())
                .fechaCreacion(producto.getFechaCreacion())
                .build();
    }
}
