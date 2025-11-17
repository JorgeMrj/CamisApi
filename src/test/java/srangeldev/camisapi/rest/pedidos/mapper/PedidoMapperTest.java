package srangeldev.camisapi.rest.pedidos.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import srangeldev.camisapi.rest.pedidos.dto.DetallePedidoDto;
import srangeldev.camisapi.rest.pedidos.dto.PedidoRequestDto;
import srangeldev.camisapi.rest.pedidos.dto.PedidoResponseDto;
import srangeldev.camisapi.rest.pedidos.mappers.PedidoMappers;
import srangeldev.camisapi.rest.pedidos.models.DetallePedido;
import srangeldev.camisapi.rest.pedidos.models.EstadoPedido;
import srangeldev.camisapi.rest.pedidos.models.Pedido;


import java.time.LocalDateTime;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


//Como no es ni un Controller ni un Service NO utilizamos Mockito
@DisplayName("PedidoMapper Tests")
public class PedidoMapperTest {

    private final PedidoMappers pedidoMappers = new PedidoMappers();

    private PedidoRequestDto pedidoRequestDto;
    private PedidoResponseDto pedidoResponseDto;
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
        pedidoResponseDto = PedidoResponseDto.builder()
                .id(1L)
                .userId(20L)
                .estado(EstadoPedido.ENVIADO)
                .createdAt(LocalDateTime.now())
                .total(100.0)
                .fechaPago((LocalDateTime.now()))
                .fechaEnvio(LocalDateTime.now())
                .detalles(List.of(detallePedidoDto))
                .build();
        pedidoRequestDto = PedidoRequestDto.builder()
                .userId(20L)
                .total(100.0)
                .detalles(List.of(detallePedidoDto))
                .build();
    }

    @Nested
    @DisplayName("Tests de toPedido")
    class ToPedidoTests{

        @Test
        @DisplayName("Convierte de PedidoRequestDto a la entidad Pedido")
        void toPedido_ok(){
            PedidoRequestDto pedidoRequestDto = PedidoRequestDto.builder()
                    .userId(1L)
                    .total(100.0)
                    .detalles(List.of(detallePedidoDto))
                    .build();

            Pedido pedido = pedidoMappers.toPedido(pedidoRequestDto);

            assertAll(
                    () -> assertEquals(pedidoRequestDto.getUserId(), pedido.getUserId()),
                    () -> assertEquals(pedidoRequestDto.getTotal(), pedido.getTotal()),
                    () -> assertEquals(detallePedidoDto.getProductoId(), detalle.getProductoId())
            );
        }
    }

    @Nested
    @DisplayName("Tests de toResponseDto")
    class ToResponseDtoTests{

        @Test
        @DisplayName("Convierte de la entidad Pedido a PedidoResponseDto")
        void toResponseDto_ok(){
            Pedido pedido = Pedido.builder()
                    .id(1L)
                    .userId(10L)
                    .estado(EstadoPedido.PAGADO)
                    .createdAt(LocalDateTime.now())
                    .total(100.0)
                    .fechaPago(LocalDateTime.now())
                    .fechaEnvio(LocalDateTime.now())
                    .detalles(List.of(detalle))
                    .build();

            PedidoResponseDto pedidoResponseDto = pedidoMappers.toResponseDto(pedido);

            assertAll(
                    () -> assertEquals(pedido.getId(), pedidoResponseDto.getId()),
                    () -> assertEquals(pedido.getUserId(), pedidoResponseDto.getUserId()),
                    () -> assertEquals(pedido.getEstado(), pedidoResponseDto.getEstado()),
                    () -> assertEquals(pedido.getCreatedAt(), pedidoResponseDto.getCreatedAt()),
                    () -> assertEquals(pedido.getTotal(), pedidoResponseDto.getTotal()),
                    () -> assertEquals(pedido.getFechaPago(), pedidoResponseDto.getFechaPago()),
                    () -> assertEquals(pedido.getFechaEnvio(), pedidoResponseDto.getFechaEnvio()),
                    () -> assertEquals(detalle.getProductoId(), detallePedidoDto.getProductoId())
            );
        }


    }
    @Nested
    @DisplayName("Tests de toDetallePedido")
    class ToDetallePedidoTests{

        @Test
        @DisplayName("Convierte de la entidad DetallePedidoDto a DetallePedido")
        void toDetallePedido_ok(){
            DetallePedidoDto dpDto = DetallePedidoDto.builder()
                    .productoId("2")
                    .nombre("Lamine Yamal")
                    .talla("L")
                    .equipo("Barcelona")
                    .precioPagado(49.99)
                    .imageUrl("/Lamine Yamal.png")
                    .build();

            DetallePedido dp = pedidoMappers.toDetallePedido(dpDto);

            assertAll(
                    () -> assertEquals(dpDto.getProductoId(), dp.getProductoId()),
                    () -> assertEquals(dpDto.getNombre(), dp.getNombre()),
                    () -> assertEquals(dpDto.getTalla(), dp.getTalla()),
                    () -> assertEquals(dpDto.getEquipo(), dp.getEquipo()),
                    () -> assertEquals(dpDto.getPrecioPagado(), dp.getPrecioPagado()),
                    () -> assertEquals(dpDto.getImageUrl(), dp.getImageUrl())
            );
        }
    }

    @Nested
    @DisplayName("Tests de toDetallePedidoDto")
    class ToDetallePedidoDtoTests{

        @Test
        @DisplayName("Convierte de la entidad DetallePedidoDto a DetallePedido")
        void toDetallePedidoDto_ok(){
            DetallePedido dp = DetallePedido.builder()
                    .productoId("2")
                    .nombre("Lamine Yamal")
                    .talla("L")
                    .equipo("Barcelona")
                    .precioPagado(49.99)
                    .imageUrl("/Lamine Yamal.png")
                    .build();

            DetallePedidoDto dpDto = pedidoMappers.toDetallePedidoDto(dp);

            assertAll(
                    () -> assertEquals(dp.getProductoId(), dpDto.getProductoId()),
                    () -> assertEquals(dp.getNombre(), dpDto.getNombre()),
                    () -> assertEquals(dp.getTalla(), dpDto.getTalla()),
                    () -> assertEquals(dp.getEquipo(), dpDto.getEquipo()),
                    () -> assertEquals(dp.getPrecioPagado(), dpDto.getPrecioPagado()),
                    () -> assertEquals(dp.getImageUrl(), dpDto.getImageUrl())
            );

        }
    }

    @Nested
    @DisplayName("Tests de toResponseList")
    class ToResponseListTests{
        @Test
        @DisplayName("Convierte una lista de entidades Pedido en una lista de PedidoResponseDto")
        void toResponseList_ok(){
            Pedido pedido = Pedido.builder()
                    .id(1L)
                    .userId(7L)
                    .estado(EstadoPedido.ENVIADO)
                    .total(100.0)
                    .detalles(List.of(detalle))
                    .build();

            List<Pedido> pedidos = List.of(pedido);
            List<PedidoResponseDto> resultado = pedidoMappers.toResponseList(pedidos);

            assertAll(
                    () -> assertEquals(1, resultado.size()),
                    () -> assertEquals(1L, resultado.get(0).getId()),
                    () -> assertEquals(7L, resultado.get(0).getUserId()),
                    () -> assertEquals(EstadoPedido.ENVIADO, resultado.get(0).getEstado()),
                    () -> assertEquals(1, resultado.get(0).getDetalles().size()),
                    () -> assertEquals("10L", resultado.get(0).getDetalles().get(0).getProductoId())
            );
        }
    }

    }



