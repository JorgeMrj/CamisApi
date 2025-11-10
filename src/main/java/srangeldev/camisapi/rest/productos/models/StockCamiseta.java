package srangeldev.camisapi.rest.productos.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class StockCamiseta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "La camiseta asociada no puede ser nula")
    private Camiseta camiseta;

    @NotNull(message = "La talla es obligatoria")
    private Tallas tallas;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;
}
