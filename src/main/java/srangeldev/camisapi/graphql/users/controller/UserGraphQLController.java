package srangeldev.camisapi.graphql.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import srangeldev.camisapi.graphql.users.mapper.UserGraphQLMapper;
import srangeldev.camisapi.graphql.users.output.UserGraphQLResponse;
import srangeldev.camisapi.rest.users.services.UserService;

import java.util.List;


@Controller
public class UserGraphQLController {

    private final UserService userService;
    private final UserGraphQLMapper mapper;

    @Autowired
    public UserGraphQLController(UserService userService, UserGraphQLMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @QueryMapping
    public List<UserGraphQLResponse> usuarios() {
        var usuarios = userService.findAll();
        return mapper.toGraphQLResponseList(usuarios);
    }

    @QueryMapping
    public UserGraphQLResponse usuario(@Argument String id) {
        Long userId = Long.parseLong(id);
        var usuario = userService.findById(userId);
        return mapper.toGraphQLResponse(usuario);
    }

    @QueryMapping
    public List<UserGraphQLResponse> usuariosPorNombre(@Argument String nombre) {
        var usuarios = userService.findByNombre(nombre);
        return mapper.toGraphQLResponseList(usuarios);
    }
}
