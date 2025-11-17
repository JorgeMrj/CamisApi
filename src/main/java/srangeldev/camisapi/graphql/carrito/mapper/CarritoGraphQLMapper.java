package srangeldev.camisapi.graphql.carrito.mapper;

import org.springframework.stereotype.Component;
import srangeldev.camisapi.graphql.carrito.input.CarritoCreateInput;
import srangeldev.camisapi.graphql.carrito.output.CarritoGraphQLResponse;
import srangeldev.camisapi.rest.carrito.dto.CarritoCreateRequestDto;
import srangeldev.camisapi.rest.carrito.dto.CarritoResponseDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Mapper simple para GraphQL de carrito (DAW)
 */
@Component
public class CarritoGraphQLMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Convierte CarritoCreateInput a CarritoCreateRequestDto
     */
    public CarritoCreateRequestDto toCreateRequestDto(CarritoCreateInput input) {
        if (input == null) {
            return null;
        }

        return CarritoCreateRequestDto.builder()
                .userId(input.getUserId())
                .items(input.getItems())
                .build();
    }

    /**
     * Convierte CarritoResponseDto a CarritoGraphQLResponse
     */
    public CarritoGraphQLResponse toGraphQLResponse(CarritoResponseDto responseDto) {
        if (responseDto == null) {
            return null;
        }

        return CarritoGraphQLResponse.builder()
                .id(responseDto.getId() != null ? responseDto.getId().toString() : null)
                .userId(responseDto.getUserId() != null ? responseDto.getUserId().toString() : null)
                .items(responseDto.getItems())
                .totalItems(responseDto.getTotalItems())
                .modificadoEn(responseDto.getModificadoEn() != null ?
                    responseDto.getModificadoEn().format(DATE_FORMATTER) : null)
                .creadoEn(responseDto.getCreadoEn() != null ?
                    responseDto.getCreadoEn().format(DATE_FORMATTER) : null)
                .build();
    }

    /**
     * Convierte lista de CarritoResponseDto a lista de CarritoGraphQLResponse
     */
    public List<CarritoGraphQLResponse> toGraphQLResponseList(List<CarritoResponseDto> responseDtos) {
        if (responseDtos == null) {
            return null;
        }

        return responseDtos.stream()
                .map(this::toGraphQLResponse)
                .toList();
    }
}
