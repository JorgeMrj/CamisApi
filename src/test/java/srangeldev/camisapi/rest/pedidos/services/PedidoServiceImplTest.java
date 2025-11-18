package srangeldev.camisapi.rest.pedidos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import srangeldev.camisapi.rest.pedidos.dto.DetallePedidoDto;
import srangeldev.camisapi.rest.pedidos.dto.PedidoRequestDto;
import srangeldev.camisapi.rest.pedidos.dto.PedidoResponseDto;
import srangeldev.camisapi.rest.pedidos.exceptions.PedidoConflictException;
import srangeldev.camisapi.rest.pedidos.exceptions.PedidoNotFoundException;
import srangeldev.camisapi.rest.pedidos.mappers.PedidoMappers;
import srangeldev.camisapi.rest.pedidos.models.DetallePedido;
import srangeldev.camisapi.rest.pedidos.models.EstadoPedido;
import srangeldev.camisapi.rest.pedidos.models.Pedido;
import srangeldev.camisapi.rest.pedidos.repository.PedidoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Podemos utilizar anotaciones @Mock
@DisplayName("PedidoServiceImpl Tests")
 class PedidoServiceImplTest {

    @Mock //Ya que son dependencias del servicio
    private PedidoRepository pedidoRepository;
    @Mock
    private PedidoMappers pedidoMappers;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Pedido pedido;
    private DetallePedido detalle;
    private PedidoResponseDto pedidoResponseDto;
    private PedidoRequestDto pedidoRequestDto;
    private DetallePedidoDto detallePedidoDto;

    @BeforeEach
    void setUp() {
        detalle = DetallePedido.builder()
                .productoId("10L")
                .nombre("Vinicius Jr")
                .talla("XL")
                .equipo("Real Madrid")
                .precioPagado(100.0)
                .imageUrl("/camisetaDeFutbol.png")
                .build();

        pedido = Pedido.builder()
                .id(1L)
                .userId("2")
                .estado(EstadoPedido.PENDIENTE_PAGO)
                .createdAt(LocalDateTime.now())
                .total(100.0)
                .detalles(List.of(detalle))
                .fechaPago((LocalDateTime.now()))
                .fechaEnvio(LocalDateTime.now())
                .build();
        detallePedidoDto = DetallePedidoDto.builder()
                .productoId("10L")
                .nombre("Vinicius Jr")
                .talla("XL")
                .equipo("Real Madrid")
                .precioPagado(100.0)
                .imageUrl("/camisetaDeFutbol.png")
                .build();
        pedidoResponseDto = PedidoResponseDto.builder()
                .id(1L)
                .userId("2")
                .estado(EstadoPedido.PAGADO)
                .createdAt(LocalDateTime.now())
                .total(100.0)
                .fechaPago((LocalDateTime.now()))
                .fechaEnvio(LocalDateTime.now())
                .detalles(List.of(detallePedidoDto))
                .build();
        pedidoRequestDto = PedidoRequestDto.builder()
                .userId("2")
                .total(100.0)
                .detalles(List.of(detallePedidoDto))
                .build();
    }
    @Nested
    @DisplayName("crearPedido")
    class CrearPedidoTest {

        @Test
        @DisplayName("Crear Pedido")
        void crearPedido_ok(){
            when(pedidoMappers.toPedido(pedidoRequestDto)).thenReturn(pedido);
            when(pedidoRepository.save(pedido)).thenReturn(pedido);
            when(pedidoMappers.toResponseDto(pedido)).thenReturn(pedidoResponseDto);

            PedidoResponseDto resultado = pedidoService.crearPedido(pedidoRequestDto);

            assertAll(
                    () -> assertEquals(EstadoPedido.PENDIENTE_PAGO, resultado.getEstado()),
                    () -> assertEquals(pedidoResponseDto.getDetalles(), resultado.getDetalles()),
                    () -> verify(pedidoRepository, times(1)).save(pedido),
                    () -> verify(pedidoMappers, times(1)).toPedido(pedidoRequestDto),
                    () -> verify(pedidoMappers, times(1)).toResponseDto(pedido)
            );
        }
    }

    @Nested
    @DisplayName("listarPedidos")
    class ListarPedidos {

        @Test
        @DisplayName("Deberia devolver todos los pedidos")
        void listarPedidos_ok(){
            when(pedidoRepository.findAll()).thenReturn(List.of(pedido));
            when(pedidoMappers.toResponseList(List.of(pedido))).thenReturn(List.of(pedidoResponseDto));

            List<PedidoResponseDto> resultados = pedidoService.listarPedidos();

            assertAll(
                    () -> assertEquals(1, resultados.size()),
                    () -> assertEquals(EstadoPedido.PAGADO, resultados.get(0).getEstado())
            );
        }

    }
    @Nested
    @DisplayName("findByUsuario")
    class findByUsuario{

        @Test
        @DisplayName("Deveria devolver todos los pedidos por usuario")
        void findByUsuario_ok(){
            when(pedidoRepository.findByUserId("2")).thenReturn(List.of(pedido));
            when(pedidoMappers.toResponseList(List.of(pedido))).thenReturn(List.of(pedidoResponseDto));


            List<PedidoResponseDto> resultados = pedidoService.findByUsuario("2");
            assertEquals(1, resultados.size());
        }
    }

    @Nested
    @DisplayName("obtenerPorId")
    class ObtenerPorId{

        @Test
        @DisplayName("Deveria devolver pedido si existe")
        void obtenerPorId_ok() {
            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
            when(pedidoMappers.toResponseDto(pedido)).thenReturn(pedidoResponseDto);

            PedidoResponseDto resultado = pedidoService.obtenerPorId(1L);

            assertAll(
                    () -> assertEquals(EstadoPedido.PENDIENTE_PAGO, resultado.getEstado())
            );
        }

        @Test
        @DisplayName("Si no existe deberia lanzar una exception")
        void obtenerPorId_notFound(){
            when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(PedidoNotFoundException.class, () -> pedidoService.obtenerPorId(1L));
        }
    }

    @Nested
    @DisplayName("actualizarEstado")
    class ActualizarEstado{

        @Test
        @DisplayName("Deveria actualizar el estado de los pedidos")
        void actualizarEstado_ok(){
            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
            when(pedidoRepository.save(pedido)).thenReturn(pedido);
            when(pedidoMappers.toResponseDto(pedido)).thenReturn(pedidoResponseDto);

            PedidoResponseDto resultado = pedidoService.actualizarEstado(1L, EstadoPedido.PAGADO);

            assertAll(
                    () -> assertEquals(EstadoPedido.PAGADO, resultado.getEstado())
            );
        }
        @Test
        @DisplayName("Deveria lanzar un exception")
        void actualizarEstado_notFound(){
            when(pedidoRepository.findById(0L)).thenReturn(Optional.empty());

            assertThrows(PedidoNotFoundException.class, () -> pedidoService.actualizarEstado(0L, EstadoPedido.PAGADO));
        }
    }

    @Nested
    @DisplayName("buscarPorEstado")
    class BuscarPorEstado{

        @Test
        @DisplayName("Devuelve pedidos por estado")
        void buscarPorEstado_ok(){
            when(pedidoRepository.findByEstado(EstadoPedido.PAGADO)).thenReturn(List.of(pedido));
            when(pedidoMappers.toResponseList(List.of(pedido))).thenReturn(List.of(pedidoResponseDto));

            List<PedidoResponseDto> resultados = pedidoService.buscarPorEstado(EstadoPedido.PAGADO);

            assertEquals(1, resultados.size());
        }
    }

    @Nested
    @DisplayName("eliminarPedido")
    class EliminarPedido{

        @Test
        @DisplayName("Elimina el pedido")
        void eliminarPedido_ok(){
            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
            pedidoService.eliminarPedido(1L);
            verify(pedidoRepository, times(1)).delete(pedido);
        }
        @Test
        @DisplayName("Si al intentar eliminar el pedido no existe")
        void eliminarPedido_notFound(){
            when(pedidoRepository.findById(3L)).thenReturn(Optional.empty());

            assertThrows(PedidoNotFoundException.class, () -> pedidoService.eliminarPedido(3L));
        }
    }

}
