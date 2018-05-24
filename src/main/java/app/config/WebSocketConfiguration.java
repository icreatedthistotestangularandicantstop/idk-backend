package app.config;

import app.websocket.notification.NotificationHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@SuppressWarnings("unused")
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final NotificationHandler notificationHandler;

    public WebSocketConfiguration(final NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(notificationHandler, "/ws/notification").setAllowedOrigins("*");
    }

}
