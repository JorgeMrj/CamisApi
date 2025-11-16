package srangeldev.camisapi.rest.productos.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import srangeldev.camisapi.rest.productos.models.EstadoProducto;
import srangeldev.camisapi.rest.productos.models.Producto;

import java.util.List;

@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {

    // Buscar productos por nombre ignorando mayúsculas y minúsculas
    List<Producto> findByNombreIgnoreCase(String nombre);

    // Buscar productos por equipo ignorando mayusculas y minusculas
    List<Producto> findByEquipoIgnoreCase(String equipo);

    // Buscar productos por estado
    List<Producto> findByEstado(EstadoProducto estado);

    // Buscar productos por talla
    List<Producto> findByTalla(String talla);
}
