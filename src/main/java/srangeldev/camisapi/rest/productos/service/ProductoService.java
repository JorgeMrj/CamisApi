package srangeldev.camisapi.rest.productos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import srangeldev.camisapi.rest.productos.dto.ProductoRequestDTO;
import srangeldev.camisapi.rest.productos.dto.ProductoResponseDTO;
import srangeldev.camisapi.rest.productos.exceptions.ProductoNotFound;
import srangeldev.camisapi.rest.productos.mapper.ProductoMapper;
import srangeldev.camisapi.rest.productos.models.EstadoProducto;
import srangeldev.camisapi.rest.productos.models.Producto;
import srangeldev.camisapi.rest.productos.repository.ProductoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    /**
     * Crea un nuevo producto en la base de datos.
     * Si no se especifica fechaCreacion, se asigna la fecha actual.
     */
    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        Producto producto = productoMapper.toEntity(dto);

        if (producto.getFechaCreacion() == null) {
            producto.setFechaCreacion(LocalDateTime.now());
        }

        producto.validarNegocio();
        Producto guardado = productoRepository.save(producto);
        return productoMapper.toDTO(guardado);
    }

    /**
     * Devuelve todos los productos del catálogo.
     */
    public List<ProductoResponseDTO> listarProductos() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    /**
     * Busca un producto por su ID.
     */
    public ProductoResponseDTO obtenerPorId(String id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFound("No se encontró el producto con ID: " + id));

        return productoMapper.toDTO(producto);
    }

    /**
     * Actualiza un producto existente.
     */
    public ProductoResponseDTO actualizarProducto(String id, ProductoRequestDTO dto) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFound("No se puede actualizar. No existe el producto con ID: " + id));

        existente.setNombre(dto.getNombre());
        existente.setEquipo(dto.getEquipo());
        existente.setTalla(dto.getTalla());
        existente.setDescripcion(dto.getDescripcion());
        existente.setPrecio(dto.getPrecio());
        existente.setImageUrl(dto.getImageUrl());
        existente.setEstado(dto.getEstado());

        existente.validarNegocio();

        Producto actualizado = productoRepository.save(existente);
        return productoMapper.toDTO(actualizado);
    }

    /**
     * Elimina un producto por su ID.
     */
    public void eliminarProducto(String id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNotFound("No se puede eliminar. No existe el producto con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    /**
     * Busca productos por nombre (contiene texto, sin distinguir mayúsculas/minúsculas).
     */
    public List<ProductoResponseDTO> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreIgnoreCase(nombre)
                .stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    /**
     * Busca productos por equipo.
     */
    public List<ProductoResponseDTO> buscarPorEquipo(String equipo) {
        return productoRepository.findByEquipoIgnoreCase(equipo)
                .stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    /**
     * Busca productos por estado (DISPONIBLE, RESERVADO, VENDIDO).
     */
    public List<ProductoResponseDTO> buscarPorEstado(EstadoProducto estado) {
        return productoRepository.findByEstado(estado)
                .stream()
                .map(productoMapper::toDTO)
                .toList();
    }
}
