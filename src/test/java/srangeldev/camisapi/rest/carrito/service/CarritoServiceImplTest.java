package srangeldev.camisapi.rest.carrito.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import srangeldev.camisapi.rest.carrito.Exceptions.CarritoBadId;
import srangeldev.camisapi.rest.carrito.Exceptions.CarritoException;
import srangeldev.camisapi.rest.carrito.Exceptions.CarritoNotFound;
import srangeldev.camisapi.rest.carrito.dto.CarritoCreateRequestDto;
import srangeldev.camisapi.rest.carrito.dto.CarritoResponseDto;
import srangeldev.camisapi.rest.carrito.dto.CarritoUpdateRequestDto;
import srangeldev.camisapi.rest.carrito.mapper.CarritoMapper;
import srangeldev.camisapi.rest.carrito.models.Carrito;
import srangeldev.camisapi.rest.carrito.repository.CarritoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CarritoServiceImpl
 * 100% cobertura - versión simple para DAW
 */
@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private CarritoMapper carritoMapper;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private final Carrito carrito = Carrito.builder()
            .id(1L)
            .userId(101L)
            .productosIds(new ArrayList<>())
            .creadoEn(LocalDateTime.now())
            .modificadoEn(LocalDateTime.now())
            .build();

    private final CarritoResponseDto carritoResponse = CarritoResponseDto.builder()
            .id(1L)
            .userId(101L)
            .productosIds(new ArrayList<>())
            .totalProductos(0)
            .creadoEn(LocalDateTime.now())
            .modificadoEn(LocalDateTime.now())
            .build();

    private final CarritoCreateRequestDto carritoCreateRequest = CarritoCreateRequestDto.builder()
            .userId(101L)
            .productosIds(new ArrayList<>())
            .build();

    private final CarritoUpdateRequestDto carritoUpdateRequest = CarritoUpdateRequestDto.builder()
            .id(1L)
            .productosIds(new ArrayList<>())
            .accion("REEMPLAZAR")
            .productoId("1")
            .build();

    @Nested
    @DisplayName("GET /api/carritos")
    class ObtenerTodosLosCarritos {
        @Test
        @DisplayName("Debe obtener todos los carritos")
        void obtenerTodos() {
            when(carritoRepository.findAll()).thenReturn(List.of(carrito));
            when(carritoMapper.toResponseDto(carrito)).thenReturn(carritoResponse);

            List<CarritoResponseDto> result = carritoService.getAll();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals(carritoResponse, result.get(0))
            );

            verify(carritoRepository, times(1)).findAll();
            verify(carritoMapper, times(1)).toResponseDto(carrito);
        }
    }

    @Nested
    @DisplayName("GET /api/carritos/{id}")
    class ObtenerCarritoPorId {
        @Test
        @DisplayName("Debe obtener un carrito por su ID")
        void obtenerPorId() {
            when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
            when(carritoMapper.toResponseDto(carrito)).thenReturn(carritoResponse);

            CarritoResponseDto result = carritoService.getById(1L);

            assertEquals(carritoResponse, result);
            verify(carritoRepository).findById(1L);
            verify(carritoMapper).toResponseDto(carrito);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el carrito no existe")
        void obtenerPorIdNoExiste() {
            when(carritoRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(CarritoNotFound.class, () -> carritoService.getById(1L));
            verify(carritoRepository).findById(1L);
        }
    }

    @Nested
    @DisplayName("POST /api/carritos")
    class CrearCarrito {
        @Test
        @DisplayName("Debe crear un nuevo carrito correctamente")
        void crearCarrito() {
            when(carritoRepository.findByUserId(101L)).thenReturn(Optional.empty());
            when(carritoMapper.toEntity(carritoCreateRequest)).thenReturn(carrito);
            when(carritoRepository.save(carrito)).thenReturn(carrito);
            when(carritoMapper.toResponseDto(carrito)).thenReturn(carritoResponse);

            CarritoResponseDto result = carritoService.save(carritoCreateRequest);

            assertEquals(carritoResponse, result);
            verify(carritoRepository).findByUserId(101L);
            verify(carritoMapper).toEntity(carritoCreateRequest);
            verify(carritoRepository).save(carrito);
            verify(carritoMapper).toResponseDto(carrito);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando ya existe carrito para el usuario")
        void crearCarritoYaExiste() {
            when(carritoRepository.findByUserId(101L)).thenReturn(Optional.of(carrito));

            assertThrows(CarritoException.class, () -> carritoService.save(carritoCreateRequest));
            verify(carritoRepository).findByUserId(101L);
        }
    }

    @Nested
    @DisplayName("PUT /api/carritos/{id}")
    class ActualizarCarrito {
        @Test
        @DisplayName("Debe actualizar un carrito correctamente")
        void actualizarCarrito() {
            when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
            when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);
            when(carritoMapper.toResponseDto(carrito)).thenReturn(carritoResponse);

            CarritoResponseDto result = carritoService.update(1L, carritoUpdateRequest);

            assertEquals(carritoResponse, result);
            verify(carritoRepository, times(2)).findById(1L);
            verify(carritoMapper).updateFromDto(carrito, carritoUpdateRequest);
            verify(carritoRepository).save(any(Carrito.class));
            verify(carritoMapper).toResponseDto(carrito);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el carrito no existe")
        void actualizarCarritoNoExiste() {
            when(carritoRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(CarritoNotFound.class, () -> carritoService.update(1L, carritoUpdateRequest));
            verify(carritoRepository, times(2)).findById(1L);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando ID ya existe en otro carrito")
        void testUpdate_IdYaExisteEnOtroCarrito() {
            CarritoUpdateRequestDto dtoConIdDiferente = CarritoUpdateRequestDto.builder()
                    .id(2L)
                    .productosIds(new ArrayList<>())
                    .accion("REEMPLAZAR")
                    .productoId("1")
                    .build();

            Carrito otroCarrito = Carrito.builder().id(2L).userId(102L).build();
            when(carritoRepository.findById(2L)).thenReturn(Optional.of(otroCarrito));

            assertThrows(CarritoException.class, () -> carritoService.update(1L, dtoConIdDiferente));
            verify(carritoRepository).findById(2L);
        }
    }

    @Nested
    @DisplayName("DELETE /api/carritos/{id}")
    class EliminarCarrito {
        @Test
        @DisplayName("Debe eliminar un carrito correctamente")
        void eliminarCarrito() {
            when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
            when(carritoMapper.toResponseDto(carrito)).thenReturn(carritoResponse);

            CarritoResponseDto result = carritoService.delete(1L);

            assertEquals(carritoResponse, result);
            verify(carritoRepository).findById(1L);
            verify(carritoRepository).delete(carrito);
            verify(carritoMapper).toResponseDto(carrito);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el carrito no existe")
        void eliminarCarritoNoExiste() {
            when(carritoRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(CarritoNotFound.class, () -> carritoService.delete(1L));
            verify(carritoRepository).findById(1L);
        }
    }

    @Nested
    @DisplayName("GET /api/carritos/usuario/{userId}")
    class ObtenerCarritoPorUsuario {
        @Test
        @DisplayName("Debe obtener carrito por ID de usuario")
        void obtenerPorUsuario() {
            when(carritoRepository.findByUserId(101L)).thenReturn(Optional.of(carrito));
            when(carritoMapper.toResponseDto(carrito)).thenReturn(carritoResponse);

            CarritoResponseDto result = carritoService.findByUserId(101L);

            assertEquals(carritoResponse, result);
            verify(carritoRepository).findByUserId(101L);
            verify(carritoMapper).toResponseDto(carrito);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando no existe carrito para el usuario")
        void obtenerPorUsuarioNoExiste() {
            when(carritoRepository.findByUserId(101L)).thenReturn(Optional.empty());

            assertThrows(CarritoBadId.class, () -> carritoService.findByUserId(101L));
            verify(carritoRepository).findByUserId(101L);
        }
    }
}
