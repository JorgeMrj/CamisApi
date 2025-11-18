package srangeldev.camisapi.rest.carrito.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import srangeldev.camisapi.rest.carrito.dto.CarritoCreateRequestDto;
import srangeldev.camisapi.rest.carrito.dto.CarritoResponseDto;
import srangeldev.camisapi.rest.carrito.dto.CarritoUpdateRequestDto;
import srangeldev.camisapi.rest.carrito.models.Carrito;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para CarritoMapper
 * 100% cobertura - versión simple para DAW
 */
@ExtendWith(MockitoExtension.class)
class CarritoMapperTest {

    private final CarritoMapper carritoMapper = new CarritoMapper();

    private final CarritoCreateRequestDto createDto = CarritoCreateRequestDto.builder()
            .userId(101L)
            .productosIds(new ArrayList<>())
            .build();

    private final Carrito carrito = Carrito.builder()
            .id(1L)
            .userId(101L)
            .productosIds(new ArrayList<>())
            .creadoEn(LocalDateTime.now())
            .modificadoEn(LocalDateTime.now())
            .build();

    private final CarritoUpdateRequestDto updateDto = CarritoUpdateRequestDto.builder()
            .id(1L)
            .productosIds(new ArrayList<>())
            .accion("REEMPLAZAR")
            .productoId("1")
            .build();

    @Nested
    @DisplayName("toEntity - Conversión de CreateRequestDto a Entidad")
    class ToEntity {

        @Test
        @DisplayName("Debe convertir CreateRequestDto a entidad correctamente")
        void debeConvertirCreateDtoAEntidad() {
            Carrito resultado = carritoMapper.toEntity(createDto);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertNull(resultado.getId()),
                    () -> assertEquals(101L, resultado.getUserId()),
                    () -> assertNotNull(resultado.getProductosIds()),
                    () -> assertTrue(resultado.getProductosIds().isEmpty()),
                    () -> assertNotNull(resultado.getCreadoEn()),
                    () -> assertNotNull(resultado.getModificadoEn())
            );
        }

        @Test
        @DisplayName("Debe retornar null cuando el DTO es null")
        void debeRetornarNullCuandoDtoEsNull() {
            Carrito resultado = carritoMapper.toEntity(null);
            assertNull(resultado);
        }
    }

    @Nested
    @DisplayName("toResponseDto - Conversión de Entidad a ResponseDto")
    class ToResponseDto {

        @Test
        @DisplayName("Debe convertir entidad a ResponseDto correctamente")
        void debeConvertirEntidadAResponseDto() {
            CarritoResponseDto resultado = carritoMapper.toResponseDto(carrito);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(1L, resultado.getId()),
                    () -> assertEquals(101L, resultado.getUserId()),
                    () -> assertNotNull(resultado.getProductosIds()),
                    () -> assertEquals(0, resultado.getTotalProductos()),
                    () -> assertEquals(carrito.getCreadoEn(), resultado.getCreadoEn()),
                    () -> assertEquals(carrito.getModificadoEn(), resultado.getModificadoEn())
            );
        }

        @Test
        @DisplayName("Debe retornar null cuando la entidad es null")
        void debeRetornarNullCuandoEntidadEsNull() {
            CarritoResponseDto resultado = carritoMapper.toResponseDto(null);
            assertNull(resultado);
        }

        @Test
        @DisplayName("Debe manejar lista de productos null")
        void debeManejarlProductosNull() {
            Carrito carritoSinProductos = Carrito.builder()
                    .id(1L)
                    .userId(101L)
                    .productosIds(null)
                    .build();

            CarritoResponseDto resultado = carritoMapper.toResponseDto(carritoSinProductos);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(0, resultado.getTotalProductos())
            );
        }
    }

    @Nested
    @DisplayName("updateFromDto - Actualización de entidad desde UpdateDto")
    class UpdateFromDto {

        @Test
        @DisplayName("Debe actualizar entidad con acción REEMPLAZAR")
        void debeActualizarConAccionReemplazar() {
            CarritoUpdateRequestDto dtoReemplazar = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .productosIds(new ArrayList<>())
                    .accion("REEMPLAZAR")
                    .productoId("1")
                    .build();

            Carrito resultado = carritoMapper.updateFromDto(carrito, dtoReemplazar);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(carrito, resultado),
                    () -> assertNotNull(resultado.getProductosIds())
            );
        }

        @Test
        @DisplayName("Debe actualizar entidad con acción LIMPIAR")
        void debeActualizarConAccionLimpiar() {
            CarritoUpdateRequestDto dtoLimpiar = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .productosIds(new ArrayList<>())
                    .accion("LIMPIAR")
                    .productoId("1")
                    .build();

            Carrito resultado = carritoMapper.updateFromDto(carrito, dtoLimpiar);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(carrito, resultado),
                    () -> assertTrue(resultado.getProductosIds().isEmpty())
            );
        }

        @Test
        @DisplayName("Debe actualizar entidad con acción AGREGAR")
        void debeActualizarConAccionAgregar() {
            CarritoUpdateRequestDto dtoAgregar = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .productosIds(new ArrayList<>())
                    .accion("AGREGAR")
                    .productoId("1")
                    .build();

            Carrito resultado = carritoMapper.updateFromDto(carrito, dtoAgregar);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(carrito, resultado),
                    () -> assertNotNull(resultado.getProductosIds())
            );
        }

        @Test
        @DisplayName("Debe actualizar entidad con acción QUITAR")
        void debeActualizarConAccionQuitar() {
            CarritoUpdateRequestDto dtoQuitar = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .productosIds(new ArrayList<>())
                    .accion("QUITAR")
                    .productoId("1")
                    .build();

            Carrito resultado = carritoMapper.updateFromDto(carrito, dtoQuitar);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(carrito, resultado),
                    () -> assertNotNull(resultado.getProductosIds())
            );
        }

        @Test
        @DisplayName("Debe manejar acción null como REEMPLAZAR")
        void debeManejadorAccionNull() {
            CarritoUpdateRequestDto dtoSinAccion = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .productosIds(new ArrayList<>())
                    .accion(null)
                    .productoId("1")
                    .build();

            Carrito resultado = carritoMapper.updateFromDto(carrito, dtoSinAccion);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(carrito, resultado),
                    () -> assertNotNull(resultado.getProductosIds())
            );
        }

        @Test
        @DisplayName("Debe retornar entidad original cuando carrito es null")
        void debeRetornarOriginalCuandoCarritoEsNull() {
            Carrito resultado = carritoMapper.updateFromDto(null, updateDto);
            assertNull(resultado);
        }

        @Test
        @DisplayName("Debe retornar entidad original cuando updateDto es null")
        void debeRetornarOriginalCuandoUpdateDtoEsNull() {
            Carrito resultado = carritoMapper.updateFromDto(carrito, null);
            assertEquals(carrito, resultado);
        }
    }
}
