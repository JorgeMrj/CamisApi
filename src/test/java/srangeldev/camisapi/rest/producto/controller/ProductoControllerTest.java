package srangeldev.camisapi.rest.producto.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import srangeldev.camisapi.rest.productos.controller.ProductoController;
import srangeldev.camisapi.rest.productos.dto.ProductoRequestDTO;
import srangeldev.camisapi.rest.productos.dto.ProductoResponseDTO;
import srangeldev.camisapi.rest.productos.models.EstadoProducto;
import srangeldev.camisapi.rest.productos.service.ProductoService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private final ProductoResponseDTO productoResponse = ProductoResponseDTO.builder()
            .id("1")
            .nombre("Camiseta Real Madrid")
            .equipo("Real Madrid")
            .precio(49.99)
            .estado(EstadoProducto.DISPONIBLE)
            .talla("S")
            .build();;

    private final ProductoRequestDTO productoRequest = ProductoRequestDTO.builder()
            .nombre("Camiseta Real Madrid")
            .equipo("Real Madrid")
            .talla("M")
            .descripcion("Camiseta oficial temporada 2025")
            .precio(89.99)
            .imageUrl("https://miweb.com/img/real-madrid-2025.png")
            .estado(EstadoProducto.DISPONIBLE)
            .talla("L")
            .build();


    @Nested
    @DisplayName("POST /api/productos")
    class CrearProducto {
        @Test
        @DisplayName("Debe crear un nuevo producto correctamente")
        void crearProducto() {
            when(productoService.crearProducto(productoRequest)).thenReturn(productoResponse);

            ResponseEntity<ProductoResponseDTO> response = productoController.crearProducto(productoRequest);

            assertAll(
                    () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                    () -> assertEquals(productoResponse, response.getBody())
            );

            verify(productoService, times(1)).crearProducto(productoRequest);
        }
    }

    @Nested
    @DisplayName("GET /api/productos")
    class ListarProductos {
        @Test
        @DisplayName("Debe listar todos los productos")
        void listarProductos() {
            when(productoService.listarProductos()).thenReturn(List.of(productoResponse));

            ResponseEntity<List<ProductoResponseDTO>> response = productoController.listarProductos();

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(1, response.getBody().size()),
                    () -> assertEquals(productoResponse, response.getBody().get(0))
            );

            verify(productoService, times(1)).listarProductos();
        }
    }

    @Nested
    @DisplayName("GET /api/productos/{id}")
    class ObtenerPorId {
        @Test
        @DisplayName("Debe obtener un producto por su ID")
        void obtenerPorId() {
            when(productoService.obtenerPorId("1")).thenReturn(productoResponse);

            ResponseEntity<ProductoResponseDTO> response = productoController.obtenerPorId("1");

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(productoResponse, response.getBody())
            );

            verify(productoService).obtenerPorId("1");
        }
    }

    @Nested
    @DisplayName("PUT /api/productos/{id}")
    class ActualizarProducto {
        @Test
        @DisplayName("Debe actualizar un producto correctamente")
        void actualizarProducto() {
            when(productoService.actualizarProducto("1", productoRequest)).thenReturn(productoResponse);

            ResponseEntity<ProductoResponseDTO> response =
                    productoController.actualizarProducto("1", productoRequest);

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(productoResponse, response.getBody())
            );

            verify(productoService).actualizarProducto("1", productoRequest);
        }
    }

    @Nested
    @DisplayName("DELETE /api/productos/{id}")
    class EliminarProducto {
        @Test
        @DisplayName("Debe eliminar un producto correctamente")
        void eliminarProducto() {
            doNothing().when(productoService).eliminarProducto("1");

            ResponseEntity<Void> response = productoController.eliminarProducto("1");

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(productoService).eliminarProducto("1");
        }
    }

    @Nested
    @DisplayName("GET /api/productos/buscar/nombre")
    class BuscarPorNombre {
        @Test
        @DisplayName("Debe buscar productos por nombre")
        void buscarPorNombre() {
            when(productoService.buscarPorNombre("Madrid")).thenReturn(List.of(productoResponse));

            ResponseEntity<List<ProductoResponseDTO>> response =
                    productoController.buscarPorNombre("Madrid");

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(1, response.getBody().size())
            );

            verify(productoService).buscarPorNombre("Madrid");
        }
    }

    @Nested
    @DisplayName("GET /api/productos/buscar/equipo")
    class BuscarPorEquipo {
        @Test
        @DisplayName("Debe buscar productos por equipo")
        void buscarPorEquipo() {
            when(productoService.buscarPorEquipo("Real Madrid")).thenReturn(List.of(productoResponse));

            ResponseEntity<List<ProductoResponseDTO>> response =
                    productoController.buscarPorEquipo("Real Madrid");

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(1, response.getBody().size())
            );

            verify(productoService).buscarPorEquipo("Real Madrid");
        }
    }

    @Nested
    @DisplayName("GET /api/productos/buscar/estado")
    class BuscarPorEstado {
        @Test
        @DisplayName("Debe buscar productos por estado")
        void buscarPorEstado() {
            when(productoService.buscarPorEstado(EstadoProducto.DISPONIBLE))
                    .thenReturn(List.of(productoResponse));

            ResponseEntity<List<ProductoResponseDTO>> response =
                    productoController.buscarPorEstado(EstadoProducto.DISPONIBLE);

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(1, response.getBody().size())
            );

            verify(productoService).buscarPorEstado(EstadoProducto.DISPONIBLE);
        }
    }

    @Nested
    @DisplayName("GET /api/productos/buscar/talla")
    class BuscarPorTalla {
        @Test
        @DisplayName("Debe buscar productos por talla")
        void buscarPorTalla() {
            when(productoService.buscarPorTalla("S"))
                    .thenReturn(List.of(productoResponse));

            ResponseEntity<List<ProductoResponseDTO>> response =
                    productoController.buscarPorTalla("S");

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(1, response.getBody().size())
            );

            verify(productoService).buscarPorTalla("S");
        }
    }
}

