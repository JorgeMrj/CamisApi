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

    @Value("${api.version:v1}")
    private String apiVersion;

    private final WebSocketHandler webSocketHandler;

    public WebSocketConfig(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/" + apiVersion + "/productos")
                .setAllowedOriginPatterns("*");
    }

    @Bean
    public WebSocketHandler productosWebSocketHandler() {
        return new srangeldev.camisapi.websocket.config.WebSocketHandler();
    }
}
