package srangeldev.camisapi.rest.carrito.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import srangeldev.camisapi.rest.carrito.Exceptions.CarritoNotFound;
import srangeldev.camisapi.rest.carrito.dto.CarritoCreateRequestDto;
import srangeldev.camisapi.rest.carrito.dto.CarritoResponseDto;
import srangeldev.camisapi.rest.carrito.dto.CarritoUpdateRequestDto;
import srangeldev.camisapi.rest.carrito.service.CarritoServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CarritoRestController
 * 100% cobertura - versión simple para DAW usando solo Mockito
 */
@ExtendWith(MockitoExtension.class)
class CarritoRestControllerTest {

    @Mock
    private CarritoServiceImpl carritoService;

    @InjectMocks
    private CarritoRestController carritoRestController;

    private final CarritoResponseDto carritoResponse = CarritoResponseDto.builder()
            .id(1L)
            .userId(101L)
            .items(Arrays.asList("item1", "item2"))
            .totalItems(2)
            .creadoEn(LocalDateTime.of(2023, 1, 1, 12, 0))
            .modificadoEn(LocalDateTime.of(2023, 1, 1, 12, 0))
            .build();

    private final CarritoCreateRequestDto carritoCreateRequest = CarritoCreateRequestDto.builder()
            .userId(101L)
            .items(Arrays.asList("item1"))
            .build();

    private final CarritoUpdateRequestDto carritoUpdateRequest = CarritoUpdateRequestDto.builder()
            .id(1L)
            .items(Arrays.asList("item1", "item2", "item3"))
            .accion("REEMPLAZAR")
            .productoId("item3")
            .build();

    @Nested
    @DisplayName("GET /carritos")
    class ObtenerTodosLosCarritos {
        @Test
        @DisplayName("Debe retornar lista de carritos con status OK")
        void obtenerTodos() {
            when(carritoService.getAll()).thenReturn(Arrays.asList(carritoResponse));

            ResponseEntity<List<CarritoResponseDto>> response = carritoRestController.getAll();

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertNotNull(response.getBody()),
                    () -> assertEquals(1, response.getBody().size()),
                    () -> assertEquals(carritoResponse, response.getBody().get(0))
            );

            verify(carritoService).getAll();
        }
    }

    @Nested
    @DisplayName("GET /carritos/{id}")
    class ObtenerCarritoPorId {
        @Test
        @DisplayName("Debe retornar carrito por ID con status OK")
        void obtenerPorId() {
            when(carritoService.getById(1L)).thenReturn(carritoResponse);

            ResponseEntity<CarritoResponseDto> response = carritoRestController.getById(1L);

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(carritoResponse, response.getBody())
            );

            verify(carritoService).getById(1L);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando carrito no existe")
        void obtenerPorIdNoExiste() {
            when(carritoService.getById(999L)).thenThrow(new CarritoNotFound(999L));

            assertThrows(CarritoNotFound.class, () -> carritoRestController.getById(999L));

            verify(carritoService).getById(999L);
        }
    }

    @Nested
    @DisplayName("GET /carritos/usuario/{userId}")
    class ObtenerCarritoPorUsuario {
        @Test
        @DisplayName("Debe retornar carrito por user ID con status OK")
        void obtenerPorUserId() {
            when(carritoService.findByUserId(101L)).thenReturn(carritoResponse);

            ResponseEntity<CarritoResponseDto> response = carritoRestController.getByUserId(101L);

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(carritoResponse, response.getBody())
            );

            verify(carritoService).findByUserId(101L);
        }
    }

    @Nested
    @DisplayName("POST /carritos")
    class CrearCarrito {
        @Test
        @DisplayName("Debe crear carrito exitosamente con status CREATED")
        void crearCarrito() {
            when(carritoService.save(carritoCreateRequest)).thenReturn(carritoResponse);

            ResponseEntity<CarritoResponseDto> response = carritoRestController.save(carritoCreateRequest);

            assertAll(
                    () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                    () -> assertEquals(carritoResponse, response.getBody())
            );

            verify(carritoService).save(carritoCreateRequest);
        }

        @Test
        @DisplayName("Debe manejar excepción del servicio")
        void crearCarritoConError() {
            when(carritoService.save(carritoCreateRequest))
                    .thenThrow(new RuntimeException("Error del servicio"));

            assertThrows(RuntimeException.class, () -> carritoRestController.save(carritoCreateRequest));

            verify(carritoService).save(carritoCreateRequest);
        }
    }

    @Nested
    @DisplayName("PUT /carritos/{id}")
    class ActualizarCarrito {
        @Test
        @DisplayName("Debe actualizar carrito exitosamente con status OK")
        void actualizarCarrito() {
            when(carritoService.update(1L, carritoUpdateRequest)).thenReturn(carritoResponse);

            ResponseEntity<CarritoResponseDto> response = carritoRestController.update(1L, carritoUpdateRequest);

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(carritoResponse, response.getBody())
            );

            verify(carritoService).update(1L, carritoUpdateRequest);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando carrito no existe")
        void actualizarCarritoNoExiste() {
            when(carritoService.update(999L, carritoUpdateRequest))
                    .thenThrow(new CarritoNotFound(999L));

            assertThrows(CarritoNotFound.class, () -> carritoRestController.update(999L, carritoUpdateRequest));

            verify(carritoService).update(999L, carritoUpdateRequest);
        }
    }

    @Nested
    @DisplayName("DELETE /carritos/{id}")
    class EliminarCarrito {
        @Test
        @DisplayName("Debe eliminar carrito exitosamente con status OK")
        void eliminarCarrito() {
            when(carritoService.delete(1L)).thenReturn(carritoResponse);

            ResponseEntity<CarritoResponseDto> response = carritoRestController.delete(1L);

            assertAll(
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertEquals(carritoResponse, response.getBody())
            );

            verify(carritoService).delete(1L);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando carrito no existe")
        void eliminarCarritoNoExiste() {
            when(carritoService.delete(999L)).thenThrow(new CarritoNotFound(999L));

            assertThrows(CarritoNotFound.class, () -> carritoRestController.delete(999L));

            verify(carritoService).delete(999L);
        }
    }
}
