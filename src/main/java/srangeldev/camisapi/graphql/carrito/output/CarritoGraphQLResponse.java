package srangeldev.camisapi.graphql.carrito.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Output GraphQL simple para carrito (DAW)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoGraphQLResponse {

    private String id;
    private String userId;
    private List<String> items;
    private Integer totalItems;
    private String modificadoEn;
    private String creadoEn;
    private Boolean isValid;

    // Método para calcular isValid
    public Boolean getIsValid() {
        return this.items != null && this.userId != null && !this.userId.trim().isEmpty();
    }
}
