package srangeldev.camisapi.rest.producto.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import srangeldev.camisapi.rest.productos.dto.ProductoRequestDTO;
import srangeldev.camisapi.rest.productos.dto.ProductoResponseDTO;
import srangeldev.camisapi.rest.productos.mapper.ProductoMapper;
import srangeldev.camisapi.rest.productos.models.EstadoProducto;
import srangeldev.camisapi.rest.productos.models.Producto;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductoMapperTest {

    private final ProductoMapper mapper = new ProductoMapper();

    @Nested
    @DisplayName("Tests de toEntity")
    class ToEntityTests {

        @Test
        @DisplayName("Convierte correctamente de ProductoRequestDTO a Producto")
        void testToEntityOk() {
            ProductoRequestDTO dto = ProductoRequestDTO.builder()
                    .nombre("Camiseta Retro")
                    .equipo("Barcelona")
                    .talla("L")
                    .descripcion("Descripción aquí")
                    .precio(49.99)
                    .imageUrl("https://example.com/img.png")
                    .estado(EstadoProducto.DISPONIBLE)
                    .build();

            Producto producto = mapper.toEntity(dto);

            assertAll(
                    () -> assertEquals(dto.getNombre(), producto.getNombre()),
                    () -> assertEquals(dto.getEquipo(), producto.getEquipo()),
                    () -> assertEquals(dto.getTalla(), producto.getTalla()),
                    () -> assertEquals(dto.getDescripcion(), producto.getDescripcion()),
                    () -> assertEquals(dto.getPrecio(), producto.getPrecio()),
                    () -> assertEquals(dto.getImageUrl(), producto.getImageUrl()),
                    () -> assertEquals(dto.getEstado(), producto.getEstado())
            );
        }
    }

    @Nested
    @DisplayName("Tests de toDTO")
    class ToDTOTests {

        @Test
        @DisplayName("Convierte correctamente de Producto a ProductoResponseDTO")
        void testToDtoOk() {
            Producto producto = Producto.builder()
                    .id("10")
                    .nombre("Camiseta Retro")
                    .equipo("Barcelona")
                    .talla("L")
                    .descripcion("Descripción aquí")
                    .precio(49.99)
                    .imageUrl("https://example.com/img.png")
                    .estado(EstadoProducto.DISPONIBLE)
                    .fechaCreacion(LocalDate.parse("2025-01-01"))
                    .build();

            ProductoResponseDTO dto = mapper.toDTO(producto);

            assertAll(
                    () -> assertEquals(producto.getId(), dto.getId()),
                    () -> assertEquals(producto.getNombre(), dto.getNombre()),
                    () -> assertEquals(producto.getEquipo(), dto.getEquipo()),
                    () -> assertEquals(producto.getTalla(), dto.getTalla()),
                    () -> assertEquals(producto.getDescripcion(), dto.getDescripcion()),
                    () -> assertEquals(producto.getPrecio(), dto.getPrecio()),
                    () -> assertEquals(producto.getImageUrl(), dto.getImageUrl()),
                    () -> assertEquals(producto.getEstado(), dto.getEstado()),
                    () -> assertEquals(producto.getFechaCreacion(), dto.getFechaCreacion())
            );
        }
    }
}
