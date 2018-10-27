package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * session websocket handler
 */
public class SessionWebSocketHandler extends TextWebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger("socket.session");
    private final Map<String, WebSocketSession> sessions = new HashMap<>(128);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String id = session.getId();
        sessions.put(id, session);
        if (logger.isDebugEnabled()) {
            logger.debug("create session {}", id);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        String id = session.getId();
        sessions.remove(id);
        if (logger.isDebugEnabled()) {
            logger.debug("close session {}", id);
        }
    }
}
