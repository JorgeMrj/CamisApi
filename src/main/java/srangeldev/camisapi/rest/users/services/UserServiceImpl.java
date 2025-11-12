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
import srangeldev.camisapi.rest.users.dto.UserCreateRequestDto;
import srangeldev.camisapi.rest.users.dto.UserResponseDto;
import srangeldev.camisapi.rest.users.dto.UserUpdateRequestDto;
import srangeldev.camisapi.rest.users.exceptions.UserBadRequest;
import srangeldev.camisapi.rest.users.exceptions.UserNotFound;
import srangeldev.camisapi.rest.users.mappers.UserMapper;
import srangeldev.camisapi.rest.users.models.User;
import srangeldev.camisapi.rest.users.repositories.UserRepository;

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
    public List<UserResponseDto> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted) {
        log.info("Obteniendo todos los usuarios con username: {}, email: {} e isDeleted: {}", username, email, isDeleted);
        
        // Aplicamos filtros según los parámetros proporcionados
        List<User> users;
        
        if (username.isPresent() && isDeleted.isPresent()) {
            // Filtro por username y isDeleted
            users = userRepository.findByUsernameContainingIgnoreCaseAndIsDeleted(username.get(), isDeleted.get());
        } else if (username.isPresent()) {
            // Solo filtro por username
            users = userRepository.findByUsernameContainingIgnoreCase(username.get());
        } else if (isDeleted.isPresent()) {
            // Solo filtro por isDeleted
            users = userRepository.findByIsDeleted(isDeleted.get());
        } else {
            // Sin filtros, devolvemos todos
            users = userRepository.findAll();
        }

        return users.stream()
                .map(userMapper::toUsuarioResponseDto)
                .toList();
    }

    @Override
    @Cacheable(key = "#id")
    public UserResponseDto findById(Long id) {
        log.info("Obteniendo usuario con id: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toUsuarioResponseDto)
                .orElseThrow();
    }

    @Override
    @Transactional
    @CachePut(key = "#result.id")
    public UserResponseDto save(UserCreateRequestDto userCreateRequestDto) {
        log.info("Guardando usuario: {}", userCreateRequestDto);
        
        // Comprobamos el usuario y sus datos
        checkUserCreate(userCreateRequestDto);

        // Mapeamos el DTO a la entidad
        User user = userMapper.toUsuario(userCreateRequestDto);
        
        // Ciframos la contraseña
        user.setPassword(user.getPassword());

        // Guardamos el usuario en la base de datos
        var userGuardado = userRepository.save(user);

        return userMapper.toUsuarioResponseDto(userGuardado);
    }

    @Override
    @Transactional
    @CachePut(key = "#id")
    public UserResponseDto update(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        log.info("Actualizando usuario con id: {}", id);
        
        // Primero lo buscamos
        var userToUpdate = userRepository.findById(id)
                .orElseThrow();

        // Comprobamos el usuario y sus datos
        checkUserUpdate(id, userUpdateRequestDto);

        // Actualizamos los datos del usuario
        var userUpdated = userMapper.toUsuario(userUpdateRequestDto, userToUpdate);
        
        // Guardamos el usuario actualizado en la base de datos
        return userMapper.toUsuarioResponseDto(userRepository.save(userUpdated));
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        log.info("Borrando usuario con id: {}", id);
        
        // Lo primero que tenemos que ver es si existe el usuario
        // Si no existe, lanzamos una excepción
        var userToDelete = userRepository.findById(id)
                .orElseThrow();
        
        // Realizamos el borrado lógico
        userRepository.updateIsDeletedById(id, true);
    }

    /**
     * Método privado para validar los datos del usuario al crear
     *
     * @param userCreateRequestDto DTO con los datos del nuevo usuario
     */
    void checkUserCreate(UserCreateRequestDto userCreateRequestDto) {
        log.info("Comprobando usuario: {}", userCreateRequestDto);
        
        // Comprobamos si ya existe un usuario con ese username
        userRepository.findByUsernameIgnoreCase(userCreateRequestDto.getUsername())
                .ifPresent(u -> {
                    throw new UserBadRequest("Ya existe un usuario con el username: " + userCreateRequestDto.getUsername());
                });
    }

    /**
     * Método privado para validar los datos del usuario al actualizar
     *
     * @param id                   ID del usuario a actualizar
     * @param userUpdateRequestDto DTO con los datos del usuario
     */
    void checkUserUpdate(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        log.info("Validando usuario con id: {}", id);
        
        // Comprobar si el nuevo username ya existe en otro usuario
        if (userUpdateRequestDto.getUsername() != null && !userUpdateRequestDto.getUsername().isEmpty()) {
            userRepository.findByUsernameIgnoreCase(userUpdateRequestDto.getUsername())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(id)) {
                            throw new UserBadRequest("El username '" + userUpdateRequestDto.getUsername() + "' ya está en uso por otro usuario.");
                        }
                    });
        }
    }
}
