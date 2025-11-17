package srangeldev.camisapi.rest.pedidos.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import srangeldev.camisapi.rest.pedidos.dto.DetallePedidoDto;
import srangeldev.camisapi.rest.pedidos.dto.PedidoRequestDto;
import srangeldev.camisapi.rest.pedidos.dto.PedidoResponseDto;
import srangeldev.camisapi.rest.pedidos.models.DetallePedido;
import srangeldev.camisapi.rest.pedidos.models.EstadoPedido;
import srangeldev.camisapi.rest.pedidos.models.Pedido;
import srangeldev.camisapi.rest.pedidos.services.PedidoService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("pedidoController Test")
public class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    private Pedido pedido;
    private DetallePedido detalle;
    private DetallePedidoDto detallePedidoDto;

    @BeforeEach
    void setUp() {
        detallePedidoDto = DetallePedidoDto.builder()
                .productoId("10L")
                .nombre("Vinicius Jr")
                .talla("XL")
                .equipo("Real Madrid")
                .precioPagado(100.0)
                .imageUrl("/camisetaDeFutbol.png")
                .build();
        detalle = DetallePedido.builder()
                .productoId("10L")
                .nombre("Vinicius Jr")
                .talla("XL")
                .equipo("Real Madrid")
                .precioPagado(100.0)
                .imageUrl("/camisetaDeFutbol.png")
                .build();
    }
    private final PedidoResponseDto pedidoResponseDto = PedidoResponseDto.builder()
            .id(1L)
            .userId(20L)
            .estado(EstadoPedido.ENVIADO)
            .createdAt(LocalDateTime.now())
            .total(100.0)
            .fechaPago((LocalDateTime.now()))
            .fechaEnvio(LocalDateTime.now())
            .detalles(List.of(detallePedidoDto))
            .build();
    private final PedidoRequestDto pedidoRequestDto = PedidoRequestDto.builder()
            .userId(20L)
            .total(100.0)
            .detalles(List.of(detallePedidoDto))
            .build();

    @Nested
    @DisplayName("POST /api/pedidos")
    class CrearPedidoTest {
        @Test
        @DisplayName("Debe crear un nuevo pedido correctamente")
        void crearPedido_ok() {
            when(pedidoService.crearPedido(pedidoRequestDto)).thenReturn(pedidoResponseDto);

            ResponseEntity<PedidoResponseDto> response = pedidoController.crearPedido(pedidoRequestDto);

            assertAll(
                    () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                    () -> assertEquals(pedidoResponseDto, response.getBody())
            );

            verify(pedidoService, times(1)).crearPedido(pedidoRequestDto);
        }
    }
    @Nested
    @DisplayName("GET /api/pedidos")
    class ListarPedidosTest {
        @Test
        @DisplayName("Debe listar todos los pedidos")
        void listarPedidos() {
            when(pedidoService.listarPedidos()).thenReturn(List.of(pedidoResponseDto));

            ResponseEntity<List<PedidoResponseDto>> response = pedidoController.listarPedidos();

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(1, response.getBody().size()),
                    () -> assertEquals(pedidoResponseDto, response.getBody().get(0))
            );

            verify(pedidoService, times(1)).listarPedidos();
        }
    }
    @Nested
    @DisplayName("GET /api/pedidos/{id}")
    class ObtenerPedidosTest {
        @Test
        @DisplayName("Debe obtener un pedido por su ID")
        void obtenerPedido_ok() {
            when(pedidoService.obtenerPorId(1L)).thenReturn(pedidoResponseDto);

            ResponseEntity<PedidoResponseDto> response = pedidoController.obtenerPedido(1L);

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(pedidoResponseDto, response.getBody())
            );

            verify(pedidoService).obtenerPorId(1L);
        }
    }

    @Nested
    @DisplayName("GET /api/pedidos/usuario/{userId}")
    class ObtenerPedidosPorUsuarioTest {
        @Test
        @DisplayName("Devolver los pedidos por usuario")
        void obtenerPedidosPorUsuario_ok(){
            when(pedidoService.findByUsuario(1L)).thenReturn(List.of(pedidoResponseDto));

            ResponseEntity<List<PedidoResponseDto>> response =
                    pedidoController.obtenerPedidosPorUsuario(1L);

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(1, response.getBody().size()),
                    () -> assertEquals(pedidoResponseDto, response.getBody().get(0))
            );

            verify(pedidoService, times(1)).findByUsuario(2L);
        }

    }
    @Nested
    @DisplayName("PUT /api/pedidos/{id}/estado")
    class ActualizarEstadoTest {
        @Test
        @DisplayName("Debe actualizar el estado del pedido")
        void actualizarPedido_ok() {
            when(pedidoService.actualizarEstado(1L, EstadoPedido.PAGADO)).thenReturn(pedidoResponseDto);

            ResponseEntity<PedidoResponseDto> response =
                    pedidoController.actualizarEstado(1L, EstadoPedido.PAGADO);

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(pedidoResponseDto, response.getBody())
            );

            verify(pedidoService).actualizarEstado(2L, EstadoPedido.ENVIADO);
        }
    }
    @Nested
    @DisplayName("GET /api/pedidos/estado")
    class ObtenerPedidosPorEstadoTest {
        @Test
        @DisplayName("Debe buscar pedidos por estado")
        void obtenerPedidosPorEstado_ok() {
            when(pedidoService.buscarPorEstado(EstadoPedido.PENDIENTE_PAGO))
                    .thenReturn(List.of(pedidoResponseDto));

            ResponseEntity<List<PedidoResponseDto>> response =
                    pedidoController.obtenerPedidosPorEstado(EstadoPedido.PENDIENTE_PAGO);

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(1, response.getBody().size())
            );

            verify(pedidoService).buscarPorEstado(EstadoPedido.PENDIENTE_PAGO);
        }
    }
    @Nested
    @DisplayName("DELETE /api/pedidos/{id}")
    class EliminarPedido {
        @Test
        @DisplayName("Debe eliminar el pedido")
        void eliminarPedido() {
            doNothing().when(pedidoService).eliminarPedido(1L);

            ResponseEntity<Void> response = pedidoController.eliminarPedido(1L);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(pedidoService).eliminarPedido(1L);
        }
    }

}


