package srangeldev.camisapi.rest.carrito.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    private Carrito carrito;
    private CarritoResponseDto carritoResponseDto;
    private CarritoCreateRequestDto carritoCreateRequestDto;
    private CarritoUpdateRequestDto carritoUpdateRequestDto;

    @BeforeEach
    void setUp() {
        carrito = Carrito.builder()
                .id(1L)
                .userId(101L)
                .items(Arrays.asList("item1", "item2"))
                .creadoEn(LocalDateTime.now())
                .modificadoEn(LocalDateTime.now())
                .build();

        carritoResponseDto = CarritoResponseDto.builder()
                .id(1L)
                .userId(101L)
                .items(Arrays.asList("item1", "item2"))
                .totalItems(2)
                .creadoEn(LocalDateTime.now())
                .modificadoEn(LocalDateTime.now())
                .build();

        carritoCreateRequestDto = CarritoCreateRequestDto.builder()
                .userId(101L)
                .items(Arrays.asList("item1"))
                .build();

        carritoUpdateRequestDto = CarritoUpdateRequestDto.builder()
                .id(1L)
                .items(Arrays.asList("item1", "item2", "item3"))
                .accion("REEMPLAZAR")
                .productoId("item3")
                .build();
    }

    @Test
    @DisplayName("getAll - Debe retornar lista de carritos")
    void testGetAll() {
        List<Carrito> carritos = Arrays.asList(carrito);
        when(carritoRepository.findAll()).thenReturn(carritos);
        when(carritoMapper.toResponseDto(carrito)).thenReturn(carritoResponseDto);

        List<CarritoResponseDto> resultado = carritoService.getAll();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getId()).isEqualTo(1L);
        verify(carritoRepository).findAll();
    }

    @Test
    @DisplayName("getById - Debe retornar carrito cuando existe")
    void testGetById_Existe() {
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoMapper.toResponseDto(carrito)).thenReturn(carritoResponseDto);

        CarritoResponseDto resultado = carritoService.getById(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(carritoRepository).findById(1L);
    }

    @Test
    @DisplayName("getById - Debe lanzar excepción cuando no existe")
    void testGetById_NoExiste() {
        when(carritoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CarritoNotFound.class, () -> carritoService.getById(1L));
    }

    @Test
    @DisplayName("save - Debe crear carrito cuando no existe")
    void testSave_Exitoso() {
        when(carritoRepository.findByUserId(101L)).thenReturn(Optional.empty());
        when(carritoMapper.toEntity(carritoCreateRequestDto)).thenReturn(carrito);
        when(carritoRepository.save(carrito)).thenReturn(carrito);
        when(carritoMapper.toResponseDto(carrito)).thenReturn(carritoResponseDto);

        CarritoResponseDto resultado = carritoService.save(carritoCreateRequestDto);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(carritoRepository).save(carrito);
    }

    @Test
    @DisplayName("save - Debe lanzar excepción cuando carrito ya existe")
    void testSave_CarritoYaExiste() {
        when(carritoRepository.findByUserId(101L)).thenReturn(Optional.of(carrito));

        assertThrows(CarritoException.class, () -> carritoService.save(carritoCreateRequestDto));
    }

    @Test
    @DisplayName("update - Debe actualizar carrito existente")
    void testUpdate_Exitoso() {
        // Given - El DTO tiene el mismo ID que el path parameter, por lo que no debe lanzar excepción
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito)); // Para la verificación de ID
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito)); // Para obtener el carrito a actualizar
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);
        when(carritoMapper.toResponseDto(carrito)).thenReturn(carritoResponseDto);

        CarritoResponseDto resultado = carritoService.update(1L, carritoUpdateRequestDto);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(carritoRepository, times(2)).findById(1L); // Se llama dos veces en el servicio
        verify(carritoRepository).save(any(Carrito.class));
        verify(carritoMapper).updateFromDto(carrito, carritoUpdateRequestDto);
    }

    @Test
    @DisplayName("update - Debe lanzar excepción cuando carrito no existe")
    void testUpdate_CarritoNoExiste() {
        // Given - Configuramos que no encuentra el carrito con el ID del DTO
        when(carritoRepository.findById(1L)).thenReturn(Optional.empty()); // Para verificación de ID
        when(carritoRepository.findById(1L)).thenReturn(Optional.empty()); // Para obtener el carrito a actualizar

        // When & Then
        assertThrows(CarritoNotFound.class, () -> carritoService.update(1L, carritoUpdateRequestDto));

        verify(carritoRepository, times(2)).findById(1L);
    }

    @Test
    @DisplayName("update - Debe lanzar excepción cuando ID ya existe en otro carrito")
    void testUpdate_IdYaExisteEnOtroCarrito() {
        // Given - Configuramos un DTO con ID diferente al path parameter
        CarritoUpdateRequestDto dtoConIdDiferente = CarritoUpdateRequestDto.builder()
                .id(2L) // ID diferente al path parameter (1L)
                .items(Arrays.asList("item1", "item2"))
                .accion("REEMPLAZAR")
                .productoId("item2")
                .build();

        // Simulamos que existe un carrito con ID 2L (diferente al path parameter 1L)
        Carrito otroCarrito = Carrito.builder().id(2L).userId(102L).build();
        when(carritoRepository.findById(2L)).thenReturn(Optional.of(otroCarrito));

        // When & Then
        assertThrows(CarritoException.class, () -> carritoService.update(1L, dtoConIdDiferente));

        verify(carritoRepository).findById(2L);
    }

    @Test
    @DisplayName("delete - Debe eliminar carrito existente")
    void testDelete_Exitoso() {
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoMapper.toResponseDto(carrito)).thenReturn(carritoResponseDto);

        CarritoResponseDto resultado = carritoService.delete(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(carritoRepository).delete(carrito);
    }

    @Test
    @DisplayName("delete - Debe lanzar excepción cuando carrito no existe")
    void testDelete_CarritoNoExiste() {
        when(carritoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CarritoNotFound.class, () -> carritoService.delete(1L));
    }

    @Test
    @DisplayName("findByUserId - Debe retornar carrito cuando existe")
    void testFindByUserId_Existe() {
        when(carritoRepository.findByUserId(101L)).thenReturn(Optional.of(carrito));
        when(carritoMapper.toResponseDto(carrito)).thenReturn(carritoResponseDto);

        CarritoResponseDto resultado = carritoService.findByUserId(101L);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(carritoRepository).findByUserId(101L);
    }

    @Test
    @DisplayName("findByUserId - Debe lanzar excepción cuando no existe")
    void testFindByUserId_NoExiste() {
        when(carritoRepository.findByUserId(101L)).thenReturn(Optional.empty());

        assertThrows(CarritoBadId.class, () -> carritoService.findByUserId(101L));
    }
}
