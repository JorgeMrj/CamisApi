package srangeldev.camisapi.rest.users.services;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import srangeldev.camisapi.rest.users.dto.UserCreateRequestDto;
import srangeldev.camisapi.rest.users.dto.UserResponseDto;
import srangeldev.camisapi.rest.users.dto.UserUpdateRequestDto;
import srangeldev.camisapi.rest.users.exceptions.UserNotFound;
import srangeldev.camisapi.rest.users.mappers.UserMapper;
import srangeldev.camisapi.rest.users.models.User;
import srangeldev.camisapi.rest.users.repositories.UserRepository;
import srangeldev.camisapi.rest.users.exceptions.UserBadRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del servicio de usuarios
 * Siguiendo el patrón establecido por el profesor con MongoDB
 */
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
        log.info("Buscando todos los usuarios");
        return userRepository.findAll().stream()
                .map(userMapper::toUsuarioResponseDto)
                .toList();
    }

    @Override
    @Cacheable(key = "#id")
    public UserResponseDto findById(ObjectId id) {
        log.info("Buscando usuario por id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("Usuario con id " + id + " no encontrado"));
        return userMapper.toUsuarioResponseDto(user);
    }

    @Override
    public List<UserResponseDto> findByNombre(String nombre) {
        log.info("Buscando usuarios por nombre: {}", nombre);
        return userRepository.findByNombre(nombre).stream()
                .map(userMapper::toUsuarioResponseDto)
                .toList();
    }

    @Override
    @CachePut(key = "#result.id")
    public UserResponseDto save(UserCreateRequestDto userCreateRequestDto) {
        log.info("Guardando usuario: {}", userCreateRequestDto);
        
        // Verificar que no exista un usuario con el mismo username
        userRepository.findByUsername(userCreateRequestDto.getUsername()).ifPresent(u -> {
            throw new UserBadRequest("Ya existe un usuario con el username " + userCreateRequestDto.getUsername());
        });

        // Crear nuevo usuario
        User user = userMapper.toUsuario(userCreateRequestDto);
        user.setId(new ObjectId()); // Asignar nuevo ObjectId
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        // Guardar en MongoDB
        User savedUser = userRepository.save(user);
        return userMapper.toUsuarioResponseDto(savedUser);
    }

    @Override
    @CachePut(key = "#id")
    public UserResponseDto update(ObjectId id, UserUpdateRequestDto userUpdateRequestDto) {
        log.info("Actualizando usuario con id {}: {}", id, userUpdateRequestDto);
        
        // Buscar usuario existente
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("Usuario con id " + id + " no encontrado"));

        // Verificar username si se está cambiando
        if (userUpdateRequestDto.getUsername() != null && 
            !userUpdateRequestDto.getUsername().isEmpty() && 
            !userUpdateRequestDto.getUsername().equals(existingUser.getUsername())) {
            
            userRepository.findByUsername(userUpdateRequestDto.getUsername()).ifPresent(u -> {
                throw new UserBadRequest("Ya existe un usuario con el username " + userUpdateRequestDto.getUsername());
            });
            existingUser.setUsername(userUpdateRequestDto.getUsername());
        }

        // Actualizar campos si vienen en el DTO
        if (userUpdateRequestDto.getNombre() != null && !userUpdateRequestDto.getNombre().isEmpty()) {
            existingUser.setNombre(userUpdateRequestDto.getNombre());
        }
        if (userUpdateRequestDto.getRoles() != null && !userUpdateRequestDto.getRoles().isEmpty()) {
            existingUser.setRoles(userUpdateRequestDto.getRoles());
        }
        
        // Actualizar fecha de modificación
        existingUser.setUpdatedAt(LocalDateTime.now());

        // Guardar cambios en MongoDB
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toUsuarioResponseDto(updatedUser);
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(ObjectId id) {
        log.info("Borrando usuario por id: {}", id);
        
        // Verificar que existe
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("Usuario con id " + id + " no encontrado"));
        
        // Borrado físico en MongoDB
        userRepository.deleteById(id);
    }
}
