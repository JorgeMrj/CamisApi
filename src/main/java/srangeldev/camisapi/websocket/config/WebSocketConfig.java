package srangeldev.camisapi.websocket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Value("${api.version:v1}") // Puedes poner un valor por defecto
    private String apiVersion;

    private final WebSocketHandler productosWebSocketHandler;

    public WebSocketConfig(WebSocketHandler productosWebSocketHandler) {
        this.productosWebSocketHandler = productosWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(productosWebSocketHandler, "/ws/" + apiVersion + "/productos")
                .setAllowedOriginPatterns("*"); // Permitir todos los orígenes
    }

    // Si necesitas exponer más handlers, solo añádelos como beans y rutas diferentes
    //@Bean
    //public WebSocketHandler productosWebSocketHandler() {
        //return new ProductosWebSocketHandler();
    //}
}
