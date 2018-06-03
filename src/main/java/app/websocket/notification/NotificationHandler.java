package app.websocket.notification;

import app.http.pojos.CustomUserDetails;
import app.http.pojos.NotificationResponse;
import app.pojo.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Scope(SCOPE_SINGLETON)
@Component
public class NotificationHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<Integer, List<WebSocketSession>> sessions = new HashMap<>();

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        final CustomUserDetails loggedUser = getLoggedUser(session);

        registerSession(session, loggedUser.getId());
    }

    public void sendToUser(final int userId, final NotificationResponse message) {
        final List<WebSocketSession> sessions = getSessionsForUser(userId);

        try {
            for (final WebSocketSession session : sessions) {
                sendMessage(session, message);
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void sendMessage(final WebSocketSession session, final NotificationResponse response) throws Exception {
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
    }

    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        unregisterSession(session);
    }

    private void registerSession(final WebSocketSession session, final int listenerUserId) {
        if (!session.isOpen()) {
            return;
        }
        if (!sessions.containsKey(listenerUserId)) {
            sessions.put(listenerUserId, new ArrayList<>());
        }

        final List<WebSocketSession> sessions = getSessionsForUser(listenerUserId);
        if (!sessions.stream().anyMatch(session1 -> session1.getId().equals(session.getId()))) {
            sessions.add(session);
        }
    }

    private void unregisterSession(final WebSocketSession closedSession) {
        for (final Map.Entry<Integer, List<WebSocketSession>> sessions : this.sessions.entrySet()) {
            sessions.getValue().removeIf(session -> session.getId().equals(closedSession.getId()));
        }
    }

    private List<WebSocketSession> getSessionsForUser(final int userId) {
        if (!sessions.containsKey(userId)) {
            return Collections.emptyList();
        }
        final List<WebSocketSession> sessions = this.sessions.get(userId);

        return sessions;
    }

    private CustomUserDetails getLoggedUser(final WebSocketSession session) {
        final CustomUserDetails loggedUser = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) session.getPrincipal()).getPrincipal();

        return loggedUser;
    }

}
