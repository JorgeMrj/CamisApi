package srangeldev.camisapi.graphql.users.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGraphQLResponse {

    private String id;
    private String nombre;
    private String username;
    private Set<String> roles;
    private String creadoEn;
    private String modificadoEn;
    private Boolean esActivo;
    private Boolean esAdmin;

    public Boolean getEsAdmin() {
        return this.roles != null && this.roles.contains("ADMIN");
    }

    public Boolean getEsActivo() {
        return this.username != null;
    }
}
