package srangeldev.camisapi.rest.carrito.service;

import srangeldev.camisapi.rest.carrito.dto.CarritoCreateRequestDto;
import srangeldev.camisapi.rest.carrito.dto.CarritoResponseDto;
import srangeldev.camisapi.rest.carrito.dto.CarritoUpdateRequestDto;

import java.util.List;


public interface CarritoService {
        List<CarritoResponseDto> getAll();
    CarritoResponseDto getById(Long id);
    CarritoResponseDto save(CarritoCreateRequestDto carrito);
    CarritoResponseDto update(Long id, CarritoUpdateRequestDto carrito);
    CarritoResponseDto delete(Long id);
    CarritoResponseDto findByUserId(Long userId);
}
