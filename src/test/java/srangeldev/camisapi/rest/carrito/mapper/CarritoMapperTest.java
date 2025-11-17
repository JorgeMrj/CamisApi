package srangeldev.camisapi.rest.carrito.mapper;

import org.junit.jupiter.api.BeforeEach;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para CarritoMapper
 * 100% cobertura - versión simple para DAW
 */
@ExtendWith(MockitoExtension.class)
class CarritoMapperTest {

    private CarritoMapper carritoMapper;

    @BeforeEach
    void setUp() {
        carritoMapper = new CarritoMapper();
    }

    @Nested
    @DisplayName("toEntity - Conversión de CreateRequestDto a Entidad")
    class ToEntity {

        @Test
        @DisplayName("Debe convertir CreateRequestDto a entidad correctamente")
        void debeConvertirCreateDtoAEntidad() {
            // Given
            CarritoCreateRequestDto createDto = CarritoCreateRequestDto.builder()
                    .userId(101L)
                    .items(Arrays.asList("item1", "item2"))
                    .build();

            // When
            Carrito resultado = carritoMapper.toEntity(createDto);

            // Then
            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertNull(resultado.getId()), // El ID no se asigna en la creación
                    () -> assertEquals(101L, resultado.getUserId()),
                    () -> assertEquals(2, resultado.getItems().size()),
                    () -> assertEquals("item1", resultado.getItems().get(0)),
                    () -> assertEquals("item2", resultado.getItems().get(1)),
                    () -> assertNotNull(resultado.getCreadoEn()),
                    () -> assertNotNull(resultado.getModificadoEn())
            );
        }

        @Test
        @DisplayName("Debe manejar CreateRequestDto con items null")
        void debeConvertirCreateDtoConItemsNull() {
            // Given
            CarritoCreateRequestDto createDto = CarritoCreateRequestDto.builder()
                    .userId(101L)
                    .items(null)
                    .build();

            // When
            Carrito resultado = carritoMapper.toEntity(createDto);

            // Then
            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(101L, resultado.getUserId()),
                    () -> assertNotNull(resultado.getItems()),
                    () -> assertTrue(resultado.getItems().isEmpty())
            );
        }

        @Test
        @DisplayName("Debe retornar null cuando CreateRequestDto es null")
        void debeRetornarNullCuandoCreateDtoEsNull() {
            // When
            Carrito resultado = carritoMapper.toEntity(null);

            // Then
            assertNull(resultado);
        }

        @Test
        @DisplayName("Debe crear una nueva lista de items (no referencia)")
        void debeCrearNuevaListaDeItems() {
            // Given
            List<String> itemsOriginales = new ArrayList<>(Arrays.asList("item1", "item2"));
            CarritoCreateRequestDto createDto = CarritoCreateRequestDto.builder()
                    .userId(101L)
                    .items(itemsOriginales)
                    .build();

            // When
            Carrito resultado = carritoMapper.toEntity(createDto);

            // Then
            assertNotSame(itemsOriginales, resultado.getItems());
            assertEquals(itemsOriginales, resultado.getItems());
        }
    }

    @Nested
    @DisplayName("toResponseDto - Conversión de Entidad a ResponseDto")
    class ToResponseDto {

        @Test
        @DisplayName("Debe convertir entidad a ResponseDto correctamente")
        void debeConvertirEntidadAResponseDto() {
            // Given
            LocalDateTime fechaCreacion = LocalDateTime.of(2023, 1, 1, 12, 0);
            LocalDateTime fechaModificacion = LocalDateTime.of(2023, 1, 1, 12, 30);

            Carrito carrito = Carrito.builder()
                    .id(1L)
                    .userId(101L)
                    .items(Arrays.asList("item1", "item2", "item3"))
                    .creadoEn(fechaCreacion)
                    .modificadoEn(fechaModificacion)
                    .build();

            // When
            CarritoResponseDto resultado = carritoMapper.toResponseDto(carrito);

            // Then
            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(1L, resultado.getId()),
                    () -> assertEquals(101L, resultado.getUserId()),
                    () -> assertEquals(3, resultado.getItems().size()),
                    () -> assertEquals("item1", resultado.getItems().get(0)),
                    () -> assertEquals("item2", resultado.getItems().get(1)),
                    () -> assertEquals("item3", resultado.getItems().get(2)),
                    () -> assertEquals(3, resultado.getTotalItems()),
                    () -> assertEquals(fechaCreacion, resultado.getCreadoEn()),
                    () -> assertEquals(fechaModificacion, resultado.getModificadoEn())
            );
        }

        @Test
        @DisplayName("Debe manejar entidad con items null")
        void debeConvertirEntidadConItemsNull() {
            // Given
            Carrito carrito = Carrito.builder()
                    .id(1L)
                    .userId(101L)
                    .items(null)
                    .creadoEn(LocalDateTime.now())
                    .modificadoEn(LocalDateTime.now())
                    .build();

            // When
            CarritoResponseDto resultado = carritoMapper.toResponseDto(carrito);

            // Then
            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(1L, resultado.getId()),
                    () -> assertEquals(101L, resultado.getUserId()),
                    () -> assertNull(resultado.getItems()),
                    () -> assertEquals(0, resultado.getTotalItems())
            );
        }

        @Test
        @DisplayName("Debe retornar null cuando entidad es null")
        void debeRetornarNullCuandoEntidadEsNull() {
            // When
            CarritoResponseDto resultado = carritoMapper.toResponseDto(null);

            // Then
            assertNull(resultado);
        }

        @Test
        @DisplayName("Debe calcular correctamente totalItems con lista vacía")
        void debeCalcularTotalItemsConListaVacia() {
            // Given
            Carrito carrito = Carrito.builder()
                    .id(1L)
                    .userId(101L)
                    .items(new ArrayList<>())
                    .creadoEn(LocalDateTime.now())
                    .modificadoEn(LocalDateTime.now())
                    .build();

            // When
            CarritoResponseDto resultado = carritoMapper.toResponseDto(carrito);

            // Then
            assertAll(
                    () -> assertNotNull(resultado.getItems()),
                    () -> assertTrue(resultado.getItems().isEmpty()),
                    () -> assertEquals(0, resultado.getTotalItems())
            );
        }
    }

    @Nested
    @DisplayName("updateFromDto - Actualización de entidad desde UpdateRequestDto")
    class UpdateFromDto {

        private Carrito carritoBase;

        @BeforeEach
        void setUpCarrito() {
            carritoBase = Carrito.builder()
                    .id(1L)
                    .userId(101L)
                    .items(new ArrayList<>(Arrays.asList("item1", "item2")))
                    .creadoEn(LocalDateTime.now())
                    .modificadoEn(LocalDateTime.now())
                    .build();
        }

        @Test
        @DisplayName("Debe reemplazar items cuando acción es REEMPLAZAR")
        void debeReemplazarItemsCuandoAccionEsReemplazar() {
            // Given
            CarritoUpdateRequestDto updateDto = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .items(Arrays.asList("item3", "item4", "item5"))
                    .accion("REEMPLAZAR")
                    .build();

            // When
            Carrito resultado = carritoMapper.updateFromDto(carritoBase, updateDto);

            // Then
            assertAll(
                    () -> assertSame(carritoBase, resultado), // Debe retornar la misma instancia
                    () -> assertEquals(3, resultado.getItems().size()),
                    () -> assertEquals("item3", resultado.getItems().get(0)),
                    () -> assertEquals("item4", resultado.getItems().get(1)),
                    () -> assertEquals("item5", resultado.getItems().get(2))
            );
        }

        @Test
        @DisplayName("Debe reemplazar items cuando acción es null (comportamiento por defecto)")
        void debeReemplazarItemsCuandoAccionEsNull() {
            // Given
            CarritoUpdateRequestDto updateDto = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .items(Arrays.asList("itemA", "itemB"))
                    .accion(null)
                    .build();

            // When
            Carrito resultado = carritoMapper.updateFromDto(carritoBase, updateDto);

            // Then
            assertAll(
                    () -> assertEquals(2, resultado.getItems().size()),
                    () -> assertEquals("itemA", resultado.getItems().get(0)),
                    () -> assertEquals("itemB", resultado.getItems().get(1))
            );
        }

        @Test
        @DisplayName("Debe agregar item cuando acción es AGREGAR y el item no existe")
        void debeAgregarItemCuandoAccionEsAgregar() {
            // Given
            CarritoUpdateRequestDto updateDto = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .accion("AGREGAR")
                    .productoId("item3")
                    .build();

            // When
            Carrito resultado = carritoMapper.updateFromDto(carritoBase, updateDto);

            // Then
            assertAll(
                    () -> assertEquals(3, resultado.getItems().size()),
                    () -> assertTrue(resultado.getItems().contains("item1")),
                    () -> assertTrue(resultado.getItems().contains("item2")),
                    () -> assertTrue(resultado.getItems().contains("item3"))
            );
        }

        @Test
        @DisplayName("No debe agregar item cuando acción es AGREGAR y el item ya existe")
        void noDebeAgregarItemCuandoYaExiste() {
            // Given
            CarritoUpdateRequestDto updateDto = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .accion("AGREGAR")
                    .productoId("item1") // Ya existe en el carrito
                    .build();

            // When
            Carrito resultado = carritoMapper.updateFromDto(carritoBase, updateDto);

            // Then
            assertAll(
                    () -> assertEquals(2, resultado.getItems().size()), // No debe cambiar el tamaño
                    () -> assertTrue(resultado.getItems().contains("item1")),
                    () -> assertTrue(resultado.getItems().contains("item2"))
            );
        }

        @Test
        @DisplayName("Debe quitar item cuando acción es QUITAR")
        void debeQuitarItemCuandoAccionEsQuitar() {
            // Given
            CarritoUpdateRequestDto updateDto = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .accion("QUITAR")
                    .productoId("item1")
                    .build();

            // When
            Carrito resultado = carritoMapper.updateFromDto(carritoBase, updateDto);

            // Then
            assertAll(
                    () -> assertEquals(1, resultado.getItems().size()),
                    () -> assertFalse(resultado.getItems().contains("item1")),
                    () -> assertTrue(resultado.getItems().contains("item2"))
            );
        }

        @Test
        @DisplayName("Debe limpiar todos los items cuando acción es LIMPIAR")
        void debeLimpiarTodosLosItemsCuandoAccionEsLimpiar() {
            // Given
            CarritoUpdateRequestDto updateDto = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .accion("LIMPIAR")
                    .build();

            // When
            Carrito resultado = carritoMapper.updateFromDto(carritoBase, updateDto);

            // Then
            assertAll(
                    () -> assertNotNull(resultado.getItems()),
                    () -> assertTrue(resultado.getItems().isEmpty())
            );
        }

        @Test
        @DisplayName("Debe retornar carrito sin cambios cuando DTO es null")
        void debeRetornarCarritoSinCambiosCuandoDtoEsNull() {
            // Given
            List<String> itemsOriginales = new ArrayList<>(carritoBase.getItems());

            // When
            Carrito resultado = carritoMapper.updateFromDto(carritoBase, null);

            // Then
            assertAll(
                    () -> assertSame(carritoBase, resultado),
                    () -> assertEquals(itemsOriginales, resultado.getItems())
            );
        }

        @Test
        @DisplayName("Debe retornar null cuando carrito es null")
        void debeRetornarNullCuandoCarritoEsNull() {
            // Given
            CarritoUpdateRequestDto updateDto = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .accion("REEMPLAZAR")
                    .items(Arrays.asList("item3"))
                    .build();

            // When
            Carrito resultado = carritoMapper.updateFromDto(null, updateDto);

            // Then
            assertNull(resultado);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando ID del DTO no coincide con ID de la entidad")
        void debeLanzarExcepcionCuandoIdNoCoincide() {
            // Given
            CarritoUpdateRequestDto updateDto = CarritoUpdateRequestDto.builder()
                    .id(999L) // ID diferente al de la entidad (1L)
                    .accion("REEMPLAZAR")
                    .items(Arrays.asList("item3"))
                    .build();

            // When & Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> carritoMapper.updateFromDto(carritoBase, updateDto)
            );

            assertEquals("El ID del carrito en la URL no coincide con el ID del DTO", exception.getMessage());
        }

        @Test
        @DisplayName("No debe lanzar excepción cuando ID del DTO es null")
        void noDebeLanzarExcepcionCuandoIdDelDtoEsNull() {
            // Given
            CarritoUpdateRequestDto updateDto = CarritoUpdateRequestDto.builder()
                    .id(null)
                    .accion("REEMPLAZAR")
                    .items(Arrays.asList("item3"))
                    .build();

            // When & Then
            assertDoesNotThrow(() -> carritoMapper.updateFromDto(carritoBase, updateDto));
        }

        @Test
        @DisplayName("No debe agregar item cuando productoId es null en acción AGREGAR")
        void noDebeAgregarItemCuandoProductoIdEsNull() {
            // Given
            List<String> itemsOriginales = new ArrayList<>(carritoBase.getItems());
            CarritoUpdateRequestDto updateDto = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .accion("AGREGAR")
                    .productoId(null)
                    .build();

            // When
            Carrito resultado = carritoMapper.updateFromDto(carritoBase, updateDto);

            // Then
            assertEquals(itemsOriginales, resultado.getItems());
        }

        @Test
        @DisplayName("No debe quitar item cuando productoId es null en acción QUITAR")
        void noDebeQuitarItemCuandoProductoIdEsNull() {
            // Given
            List<String> itemsOriginales = new ArrayList<>(carritoBase.getItems());
            CarritoUpdateRequestDto updateDto = CarritoUpdateRequestDto.builder()
                    .id(1L)
                    .accion("QUITAR")
                    .productoId(null)
                    .build();

            // When
            Carrito resultado = carritoMapper.updateFromDto(carritoBase, updateDto);

            // Then
            assertEquals(itemsOriginales, resultado.getItems());
        }
    }
}
