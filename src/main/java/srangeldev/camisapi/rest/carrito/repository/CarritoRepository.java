package srangeldev.camisapi.rest.carrito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import srangeldev.camisapi.rest.carrito.models.Carrito;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    // Buscar carrito por usuario
    Optional<Carrito> findByUserId(Long userId);
}