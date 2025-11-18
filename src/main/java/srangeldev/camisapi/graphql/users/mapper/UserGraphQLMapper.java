package srangeldev.camisapi.graphql.users.mapper;

import org.springframework.stereotype.Component;
import srangeldev.camisapi.graphql.users.output.UserGraphQLResponse;
import srangeldev.camisapi.rest.users.dto.UserResponseDto;

import java.util.List;


@Component
public class UserGraphQLMapper {

    public UserGraphQLResponse toGraphQLResponse(UserResponseDto dto) {
        return UserGraphQLResponse.builder()
                .id(dto.getId() != null ? dto.getId().toString() : null)
                .nombre(dto.getNombre())
                .username(dto.getUsername())
                .roles(dto.getRoles() != null ?
                    dto.getRoles().stream().map(rol -> rol.toString()).collect(java.util.stream.Collectors.toSet()) : null)
                .creadoEn(dto.getCreatedAt() != null ? dto.getCreatedAt().toString() : null)
                .modificadoEn(dto.getUpdatedAt() != null ? dto.getUpdatedAt().toString() : null)
                .build();
    }

    public List<UserGraphQLResponse> toGraphQLResponseList(List<UserResponseDto> dtos) {
        return dtos.stream()
                .map(this::toGraphQLResponse)
                .toList();
    }
}
