package srangeldev.camisapi.rest.stockTalla;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import srangeldev.camisapi.rest.productos.models.Camisetas;

public class StockTalla {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @NotNull(message = "La camiseta asociada no puede ser nula")
    private Camisetas camiseta;

    @NotNull(message = "La talla es obligatoria")
    private Tallas tallas;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;
}
