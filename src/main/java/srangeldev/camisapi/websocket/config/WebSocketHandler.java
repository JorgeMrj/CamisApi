package srangeldev.camisapi.websocket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("Cliente conectado: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("Cliente desconectado: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Mensaje recibido de " + session.getId() + ": " + message.getPayload());
        broadcast(message.getPayload()); // reenvía a todos
    }

    // Enviar mensaje a todos los clientes conectados
    public void broadcast(String payload) {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(payload));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Enviar cualquier objeto como JSON
    public void broadcastObject(Object obj) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            broadcast(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
