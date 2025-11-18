package srangeldev.camisapi.rest.producto.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.WebSocketHandler;
import srangeldev.camisapi.rest.productos.dto.ProductoRequestDTO;
import srangeldev.camisapi.rest.productos.dto.ProductoResponseDTO;
import srangeldev.camisapi.rest.productos.mapper.ProductoMapper;
import srangeldev.camisapi.rest.productos.models.EstadoProducto;
import srangeldev.camisapi.rest.productos.models.Producto;
import srangeldev.camisapi.rest.productos.repository.ProductoRepository;
import srangeldev.camisapi.rest.productos.exceptions.ProductoNotFound;
import srangeldev.camisapi.rest.productos.service.ProductoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de ProductoService")
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoMapper productoMapper;

    @Mock
    private srangeldev.camisapi.websocket.config.WebSocketHandler webSocketHandler;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private ProductoRequestDTO requestDTO;
    private ProductoResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id("1")
                .nombre("Camiseta Real Madrid")
                .equipo("Real Madrid")
                .talla("M")
                .descripcion("Camiseta oficial")
                .precio(50.0)
                .imageUrl("url")
                .estado(EstadoProducto.DISPONIBLE)
                .fechaCreacion(LocalDate.now())
                .build();

        requestDTO = ProductoRequestDTO.builder()
                .nombre("Camiseta Real Madrid")
                .equipo("Real Madrid")
                .talla("M")
                .descripcion("Camiseta oficial")
                .precio(50.0)
                .imageUrl("url")
                .estado(EstadoProducto.DISPONIBLE)
                .build();

        responseDTO = ProductoResponseDTO.builder()
                .id("1")
                .nombre("Camiseta Real Madrid")
                .equipo("Real Madrid")
                .talla("M")
                .descripcion("Camiseta oficial")
                .precio(50.0)
                .imageUrl("url")
                .estado(EstadoProducto.DISPONIBLE)
                .fechaCreacion(LocalDate.now())
                .build();
    }

    // ------------------------------------------------------
    // CREAR PRODUCTO
    // ------------------------------------------------------
    @Nested
    @DisplayName("crearProducto")
    class CrearProducto {

        @Test
        @DisplayName("Debería crear un producto correctamente")
        void crearProducto_ok() {
            when(productoMapper.toEntity(requestDTO)).thenReturn(producto);
            when(productoRepository.save(producto)).thenReturn(producto);
            when(productoMapper.toDTO(producto)).thenReturn(responseDTO);

            ProductoResponseDTO resultado = productoService.crearProducto(requestDTO);

            assertAll(
                    () -> assertEquals(responseDTO.getNombre(), resultado.getNombre()),
                    () -> verify(productoRepository, times(1)).save(producto),
                    () -> verify(webSocketHandler, times(1))
                            .enviarMensajeATodos("Producto creado:" + producto.getNombre())
            );
        }
    }

    // ------------------------------------------------------
    // LISTAR PRODUCTOS
    // ------------------------------------------------------
    @Nested
    @DisplayName("listarProductos")
    class ListarProductos {

        @Test
        @DisplayName("Debería devolver todos los productos")
        void listarProductos_ok() {
            when(productoRepository.findAll()).thenReturn(List.of(producto));
            when(productoMapper.toDTO(producto)).thenReturn(responseDTO);

            List<ProductoResponseDTO> resultados = productoService.listarProductos();

            assertAll(
                    () -> assertEquals(1, resultados.size()),
                    () -> assertEquals("Camiseta Real Madrid", resultados.get(0).getNombre()),
                    () -> verify(webSocketHandler, times(1))
                            .enviarMensajeATodos("Productos listados correctamente")
            );
        }
    }

    // ------------------------------------------------------
    // OBTENER POR ID
    // ------------------------------------------------------
    @Nested
    @DisplayName("obtenerPorId")
    class ObtenerPorId {

        @Test
        @DisplayName("Debería devolver producto si existe")
        void obtenerPorId_ok() {
            when(productoRepository.findById("1")).thenReturn(Optional.of(producto));
            when(productoMapper.toDTO(producto)).thenReturn(responseDTO);

            ProductoResponseDTO resultado = productoService.obtenerPorId("1");

            assertAll(
                    () -> assertEquals("Camiseta Real Madrid", resultado.getNombre()),
                    () -> verify(webSocketHandler, times(1))
                            .enviarMensajeATodos("Producto obtenido con id:1")
            );
        }

        @Test
        @DisplayName("Debería lanzar excepción si no existe")
        void obtenerPorId_notFound() {
            when(productoRepository.findById("2")).thenReturn(Optional.empty());

            assertThrows(ProductoNotFound.class, () -> productoService.obtenerPorId("2"));
            verify(webSocketHandler, never()).enviarMensajeATodos(anyString());
        }
    }

    // ------------------------------------------------------
    // ACTUALIZAR PRODUCTO
    // ------------------------------------------------------
    @Nested
    @DisplayName("actualizarProducto")
    class ActualizarProducto {

        @Test
        @DisplayName("Debería actualizar un producto existente")
        void actualizarProducto_ok() {
            when(productoRepository.findById("1")).thenReturn(Optional.of(producto));
            when(productoRepository.save(producto)).thenReturn(producto);
            when(productoMapper.toDTO(producto)).thenReturn(responseDTO);

            ProductoResponseDTO resultado = productoService.actualizarProducto("1", requestDTO);

            assertAll(
                    () -> assertEquals("Camiseta Real Madrid", resultado.getNombre()),
                    () -> verify(productoRepository, times(1)).save(producto),
                    () -> verify(webSocketHandler, times(1))
                            .enviarMensajeATodos("Producto actualizado con id:1")
            );
        }

        @Test
        @DisplayName("Debería lanzar excepción si el producto no existe")
        void actualizarProducto_notFound() {
            when(productoRepository.findById("2")).thenReturn(Optional.empty());

            assertThrows(ProductoNotFound.class, () -> productoService.actualizarProducto("2", requestDTO));
            verify(webSocketHandler, never()).enviarMensajeATodos(anyString());
        }
    }

    // ------------------------------------------------------
    // ELIMINAR PRODUCTO
    // ------------------------------------------------------
    @Nested
    @DisplayName("eliminarProducto")
    class EliminarProducto {

        @Test
        @DisplayName("Debería eliminar el producto si existe")
        void eliminarProducto_ok() {
            when(productoRepository.findById("1")).thenReturn(Optional.of(producto));

            productoService.eliminarProducto("1");

            verify(productoRepository, times(1)).findById("1");
            verify(productoRepository, times(1)).deleteById("1");
            verify(webSocketHandler, times(1))
                    .enviarMensajeATodos("Producto eliminado con id1");
        }

        @Test
        @DisplayName("Debería lanzar excepción si el producto no existe")
        void eliminarProducto_notFound() {

            when(productoRepository.findById("1"))
                    .thenReturn(Optional.empty());

            ProductoNotFound exception = assertThrows(
                    ProductoNotFound.class,
                    () -> productoService.eliminarProducto("1")
            );

            assertTrue(exception.getMessage().contains("No se encontro el producto"));

            verify(productoRepository, times(1)).findById("1");
            verify(productoRepository, never()).deleteById(anyString());
        }
    }

    // ------------------------------------------------------
    // BUSCAR POR NOMBRE
    // ------------------------------------------------------
    @Nested
    @DisplayName("buscarPorNombre")
    class BuscarPorNombre {

        @Test
        @DisplayName("Debería devolver productos por nombre")
        void buscarPorNombre_ok() {
            when(productoRepository.findByNombreIgnoreCase("Camiseta Real Madrid"))
                    .thenReturn(List.of(producto));

            when(productoMapper.toDTO(producto)).thenReturn(responseDTO);

            List<ProductoResponseDTO> resultados = productoService.buscarPorNombre("Camiseta Real Madrid");

            assertEquals(1, resultados.size());
        }
    }

    // ------------------------------------------------------
    // BUSCAR POR EQUIPO
    // ------------------------------------------------------
    @Nested
    @DisplayName("buscarPorEquipo")
    class BuscarPorEquipo {

        @Test
        @DisplayName("Debería devolver productos por equipo")
        void buscarPorEquipo_ok() {
            when(productoRepository.findByEquipoIgnoreCase("Real Madrid"))
                    .thenReturn(List.of(producto));

            when(productoMapper.toDTO(producto)).thenReturn(responseDTO);

            List<ProductoResponseDTO> resultados = productoService.buscarPorEquipo("Real Madrid");

            assertEquals(1, resultados.size());
        }
    }

    // ------------------------------------------------------
    // BUSCAR POR ESTADO
    // ------------------------------------------------------
    @Nested
    @DisplayName("buscarPorEstado")
    class BuscarPorEstado {

        @Test
        @DisplayName("Debería devolver productos por estado")
        void buscarPorEstado_ok() {
            when(productoRepository.findByEstado(EstadoProducto.DISPONIBLE))
                    .thenReturn(List.of(producto));

            when(productoMapper.toDTO(producto)).thenReturn(responseDTO);

            List<ProductoResponseDTO> resultados = productoService.buscarPorEstado(EstadoProducto.DISPONIBLE);

            assertEquals(1, resultados.size());
        }
    }

    @Nested
    @DisplayName("buscarPorTalla")
    class BuscarPorTalla {

        @Test
        @DisplayName("Debería devolver productos por talla")
        void buscarProductosPorTalla_ok() {
            when(productoRepository.findByTalla("M")).thenReturn(List.of(producto));
            when(productoMapper.toDTO(producto)).thenReturn(responseDTO);

            List<ProductoResponseDTO> resultados = productoService.buscarPorTalla("M");

            assertEquals(1, resultados.size());
        }
    }
}
