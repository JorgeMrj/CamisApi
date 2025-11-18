package srangeldev.camisapi.rest.productos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Productos", description = "API para gestión de productos de camisetas de fútbol")
public class ProductoController {

    private final ProductoService productoService;

    @Operation(
        summary = "Listar todos los productos",
        description = "Obtiene una lista completa de todos los productos disponibles"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de productos recuperada exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
            )
        )
    })
    @GetMapping({"", "/"})
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        log.info("GET: Listando todas los productos");
        List<ProductoResponseDTO> productos = productoService.listarProductos();
        return ResponseEntity.ok(productos);
    }

    @Operation(
        summary = "Obtener producto por ID",
        description = "Recupera un producto específico mediante su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductoResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(
        @Parameter(description = "ID único del producto", required = true) @PathVariable String id) {
        log.info("GET: Obteniendo un producto por ID {}", id);
        ProductoResponseDTO producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    @Operation(
        summary = "Crear nuevo producto",
        description = "Crea un nuevo producto con los datos proporcionados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Producto creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductoResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        )
    })
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(
        @Parameter(description = "Datos del producto a crear", required = true)
        @Valid @RequestBody ProductoRequestDTO productoRequestDTO) {
        log.info("POST: Creando un producto {}", productoRequestDTO.getNombre());
        ProductoResponseDTO nuevoProducto = productoService.crearProducto(productoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @Operation(
        summary = "Actualizar producto existente",
        description = "Actualiza un producto existente con los nuevos datos proporcionados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductoResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
        @Parameter(description = "ID del producto a actualizar", required = true) @PathVariable String id,
        @Parameter(description = "Nuevos datos del producto", required = true)
        @Valid @RequestBody ProductoRequestDTO productoRequestDTO) {
        log.info("PUT: Actualizando un producto con id: {}", id);
        ProductoResponseDTO actualizado = productoService.actualizarProducto(id, productoRequestDTO);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(
        summary = "Eliminar producto por ID",
        description = "Elimina un producto específico del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Producto eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
        @Parameter(description = "ID del producto a eliminar", required = true) @PathVariable String id) {
        log.info("DELETE: Eliminando un producto con id: {}", id);
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Buscar productos por nombre",
        description = "Busca productos que contengan el nombre especificado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Búsqueda realizada exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
            )
        )
    })
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombre(
        @Parameter(description = "Nombre del producto a buscar", required = true) @RequestParam String nombre) {
        log.info("GET: Buscando un producto por nombre {}", nombre);
        List<ProductoResponseDTO> resultados = productoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(resultados);
    }

    @Operation(
        summary = "Buscar productos por equipo",
        description = "Busca productos que pertenezcan al equipo especificado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Búsqueda realizada exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
            )
        )
    })
    @GetMapping("/buscar/equipo")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorEquipo(
        @Parameter(description = "Nombre del equipo", required = true) @RequestParam String equipo) {
        log.info("GET: Buscando un producto por equipo {}", equipo);
        List<ProductoResponseDTO> resultados = productoService.buscarPorEquipo(equipo);
        return ResponseEntity.ok(resultados);
    }

    @Operation(
        summary = "Buscar productos por estado",
        description = "Filtra productos según su estado (DISPONIBLE, AGOTADO, DESCONTINUADO)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Búsqueda realizada exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
            )
        )
    })
    @GetMapping("/buscar/estado")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorEstado(
        @Parameter(description = "Estado del producto", required = true) @RequestParam EstadoProducto estado) {
        log.info("GET: Buscando un producto por estado {}", estado);
        List<ProductoResponseDTO> resultados = productoService.buscarPorEstado(estado);
        return ResponseEntity.ok(resultados);
    }

    @Operation(
        summary = "Buscar productos por talla",
        description = "Filtra productos según la talla especificada (XS, S, M, L, XL, XXL)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Búsqueda realizada exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
            )
        )
    })
    @GetMapping("/buscar/talla")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorTalla(
        @Parameter(description = "Talla del producto", required = true) @RequestParam String talla) {
        log.info("GET: Buscando un producto por talla {}", talla);
        List<ProductoResponseDTO> resultados = productoService.buscarPorTalla(talla);
        return ResponseEntity.ok(resultados);
    }
}

