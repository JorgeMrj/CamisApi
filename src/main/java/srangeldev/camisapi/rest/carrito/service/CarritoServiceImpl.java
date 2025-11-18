package srangeldev.camisapi.rest.carrito.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
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
import java.util.List;
import java.util.Optional;


@Service
@CacheConfig(cacheNames = "carritos")
public class CarritoServiceImpl implements CarritoService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CarritoRepository carritoRepository;
    private final CarritoMapper carritoMapper;

    @Autowired
    public CarritoServiceImpl(CarritoRepository carritoRepository, CarritoMapper carritoMapper) {
        this.carritoMapper = carritoMapper;
        this.carritoRepository = carritoRepository;
    }

    @Override
    public List<CarritoResponseDto> getAll() {
        logger.info("Obteniendo todas las carritos");
        return carritoRepository.findAll().stream().map(carritoMapper::toResponseDto).toList();
    }

    @Override
    public CarritoResponseDto getById(Long id) {
        logger.info("Buscando carrito por id: " + id);
        Carrito carrito = carritoRepository.findById(id).orElseThrow(
                () -> new CarritoNotFound(id)
        );

        return carritoMapper.toResponseDto(carrito);
    }

    @Override
    public CarritoResponseDto save(CarritoCreateRequestDto carrito) {
        logger.info("=== INICIO SAVE Carrito ===");
        logger.info("Guardando carrito: {}", carrito);
        logger.info("id recibido: {}", carrito != null ? carrito.getUserId() : "NULL");

        try {
            Optional<Carrito> existente = carritoRepository.findByUserId(carrito.getUserId());
            logger.info("Verificación de carrito existente: {}", existente.isPresent() ? "EXISTE" : "NO EXISTE");

            if (existente.isPresent()) {
                logger.warn("Carrito ya existe con nombre: {}", carrito.getUserId());
                throw new CarritoException("Ya existe una carrito con el id de usuario: " + carrito.getUserId()) {
                };
            }

            logger.info("Creando nuevo carrito con mapper");
            Carrito nueva = carritoMapper.toEntity(carrito);
            logger.info("carrito mapeado: {}", nueva);

            logger.info("Guardando en repositorio");
            Carrito guardada = carritoRepository.save(nueva);
            logger.info("carrito guardado: {}", guardada);

            CarritoResponseDto response = carritoMapper.toResponseDto(guardada);
            logger.info("Response mapeada: {}", response);
            logger.info("=== FIN SAVE CARRITO EXITOSO ===");

            return response;
        } catch (Exception e) {
            logger.error("=== ERROR EN SAVE Carrito ===");
            logger.error("Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public CarritoResponseDto update(Long id, CarritoUpdateRequestDto carrito) {
        logger.info("Actualizando Carrito con id: " + id);

        Optional<Carrito> existenteConId = carritoRepository.findById(carrito.getId());
        if (existenteConId.isPresent() && !existenteConId.get().getId().equals(id)) {
            throw new CarritoException("El carrito con este usuario ya existe") {
            };
        }

        Carrito actualizado = carritoRepository.findById(id).orElseThrow(
                () -> new CarritoNotFound(id)
        );

        // Usar el mapper para actualizar la entidad
        carritoMapper.updateFromDto(actualizado, carrito);
        actualizado.setModificadoEn(LocalDateTime.now());
        return carritoMapper.toResponseDto(carritoRepository.save(actualizado));
    }

    @Override
    public CarritoResponseDto delete(Long id) {
        logger.info("Eliminando carrito con id: " + id);
        Carrito borrada = carritoRepository.findById(id).orElseThrow(
                () -> new CarritoNotFound(id)
        );

        carritoRepository.delete(borrada);
        return carritoMapper.toResponseDto(borrada);
    }

    @Override
    public CarritoResponseDto findByUserId(Long userId) {
        logger.info("Buscando carrito por user id: " + userId);
        Carrito carrito = carritoRepository.findByUserId(userId).orElseThrow(
                () -> new CarritoBadId("No se encontro carrito con nombre: " + userId)
        );
        return carritoMapper.toResponseDto(carrito);
    }
    }




