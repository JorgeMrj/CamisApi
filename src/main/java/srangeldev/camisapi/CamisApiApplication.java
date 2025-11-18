package srangeldev.camisapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
    "srangeldev.camisapi.rest.carrito.repository",
    "srangeldev.camisapi.rest.pedidos.repository"
})
@EnableMongoRepositories(basePackages = {
    "srangeldev.camisapi.rest.users.repositories",
    "srangeldev.camisapi.rest.productos.repository"
})
@EnableCaching
public class CamisApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamisApiApplication.class, args);
    }

}
