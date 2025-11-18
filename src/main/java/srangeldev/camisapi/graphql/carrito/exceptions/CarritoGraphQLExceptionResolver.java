package srangeldev.camisapi.graphql.carrito.exceptions;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.stereotype.Component;
import srangeldev.camisapi.rest.carrito.Exceptions.CarritoNotFound;

/**
 * Maneja errores de GraphQL para carrito (versión simple)
 */
@Component
public class CarritoGraphQLExceptionResolver {

    @GraphQlExceptionHandler
    public GraphQLError handleCarritoNotFound(CarritoNotFound ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleGenericException(Exception ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .message("Error: " + ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .build();
    }
}
