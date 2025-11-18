package srangeldev.camisapi.rest.productos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import srangeldev.camisapi.rest.productos.dto.ProductoRequestDTO;
import srangeldev.camisapi.rest.productos.dto.ProductoResponseDTO;
import srangeldev.camisapi.rest.productos.models.EstadoProducto;
import srangeldev.camisapi.rest.productos.service.ProductoService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    /**
     * Listar todos los productos
     */
    @GetMapping({"", "/"})
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        log.info("GET: Listando todas los productos");
        List<ProductoResponseDTO> productos = productoService.listarProductos();
        return ResponseEntity.ok(productos);
    }


    /**
     * Obtener un producto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable String id) {
        log.info("GET: Obteniendo un producto por ID {}", id);
        ProductoResponseDTO producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    /**
     * Crear un nuevo producto
     */
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(
            @Valid
            @RequestBody ProductoRequestDTO productoRequestDTO) {
        log.info("POST: Creando un producto {}", productoRequestDTO.getNombre());

        ProductoResponseDTO nuevoProducto = productoService.crearProducto(productoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    /**
     * Actualizar un producto existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable String id,
            @Valid @RequestBody ProductoRequestDTO productoRequestDTO) {
        log.info("PUT: Actualizando un producto con id: {}", id);

        ProductoResponseDTO actualizado = productoService.actualizarProducto(id, productoRequestDTO);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Eliminar un producto por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String id) {
        log.info("DELETE: Eliminando un producto con id: {}", id);
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Buscar productos por nombre
     */
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombre(@RequestParam String nombre) {
        log.info("GET: Buscando un producto por nombre {}", nombre);
        List<ProductoResponseDTO> resultados = productoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(resultados);
    }

    /**
     * Buscar productos por equipo
     */
    @GetMapping("/buscar/equipo")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorEquipo(@RequestParam String equipo) {
        log.info("GET: Buscando un producto por equipo {}", equipo);
        List<ProductoResponseDTO> resultados = productoService.buscarPorEquipo(equipo);
        return ResponseEntity.ok(resultados);
    }

    /**
     * Buscar productos por estado
     */
    @GetMapping("/buscar/estado")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorEstado(@RequestParam EstadoProducto estado) {
        log.info("GET: Buscando un producto por estado {}", estado);
        List<ProductoResponseDTO> resultados = productoService.buscarPorEstado(estado);
        return ResponseEntity.ok(resultados);
    }

    /**
     * Buscar productos por talla
     */
    @GetMapping("/buscar/talla")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorTalla(@RequestParam String talla) {
        log.info("GET: Buscando un producto por talla {}", talla);
        List<ProductoResponseDTO> resultados = productoService.buscarPorTalla(talla);
        return ResponseEntity.ok(resultados);
    }
}

