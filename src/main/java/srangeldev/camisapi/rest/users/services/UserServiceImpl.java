package srangeldev.camisapi.rest.users.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import srangeldev.camisapi.rest.users.dto.UserCreateRequestDto;
import srangeldev.camisapi.rest.users.dto.UserResponseDto;
import srangeldev.camisapi.rest.users.dto.UserUpdateRequestDto;
import srangeldev.camisapi.rest.users.mappers.UserMapper;
import srangeldev.camisapi.rest.users.models.User;
import srangeldev.camisapi.rest.users.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@CacheConfig(cacheNames = {"users"})
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users.stream()
                .map(userMapper::toUsuarioResponseDto)
                .toList();
    }

    @Override
    @Cacheable(key = "#id")
    public UserResponseDto findById(String id) {
        log.info("Buscando usuario por id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFound("Usuario con id " + id + " no encontrado"));
        return userMapper.toUsuarioResponseDto(user);
    }

    @Override
    @CachePut(key = "#result.id")
    public UserResponseDto save(UserCreateRequestDto userCreateRequestDto) {
        log.info("Guardando usuario: {}", userCreateRequestDto);
        userRepository.findByUsernameIgnoreCase(userCreateRequestDto.getUsername()).ifPresent(u -> {
            throw new UserBadRequest("Ya existe un usuario con el username " + userCreateRequestDto.getUsername());
        });
        User user = userMapper.toUsuario(userCreateRequestDto);
        user.setPassword(user.getPassword());
        return userMapper.toUsuarioResponseDto(userRepository.save(user));
    }

    @Override
    @CachePut(key = "#id")
    public UserResponseDto update(String id, UserUpdateRequestDto userUpdateRequestDto) {
        log.info("Actualizando usuario con id {}: {}", id, userUpdateRequestDto);
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFound("Usuario con id " + id + " no encontrado"));

        if (userUpdateRequestDto.getUsername() != null && !userUpdateRequestDto.getUsername().isEmpty() && !userUpdateRequestDto.getUsername().equals(existingUser.getUsername())) {
            userRepository.findByUsernameIgnoreCase(userUpdateRequestDto.getUsername()).ifPresent(u -> {
                throw new UserBadRequest("Ya existe un usuario con el username " + userUpdateRequestDto.getUsername());
            });
            existingUser.setUsername(userUpdateRequestDto.getUsername());
        }

        if (userUpdateRequestDto.getUsername() != null && !userUpdateRequestDto.getUsername().isEmpty()) {
            existingUser.setUsername(userUpdateRequestDto.getUsername());
        }
        if (userUpdateRequestDto.getNombre() != null && !userUpdateRequestDto.getNombre().isEmpty()) {
            existingUser.setNombre(userUpdateRequestDto.getNombre());
        }
        if (userUpdateRequestDto.getRoles() != null && !userUpdateRequestDto.getRoles().isEmpty()) {
            existingUser.setRoles(userUpdateRequestDto.getRoles());
        }

        return userMapper.toUsuarioResponseDto(userRepository.save(existingUser));
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(String id) {
        log.info("Borrando usuario por id: {}", id);
        userRepository.findById(id).orElseThrow(() -> new UserNotFound("Usuario con id " + id + " no encontrado"));
        userRepository.deleteById(id);
    }
}
