package srangeldev.camisapi.graphql.carrito.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


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

    public Boolean getIsValid() {
        return this.items != null && this.userId != null && !this.userId.trim().isEmpty();
    }
}
