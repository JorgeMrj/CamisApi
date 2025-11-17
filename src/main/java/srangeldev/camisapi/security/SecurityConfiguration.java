package srangeldev.camisapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                // Dar permiso a los errores
                .authorizeHttpRequests(request -> request.requestMatchers("/error/**")
                        .permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers("/saludo/**")
                        .permitAll())
                // Resto de peticiones
                .authorizeHttpRequests(request -> request.anyRequest()
                        .authenticated());

        return http.build();
    }
}