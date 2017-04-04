package com.horyu1234.kkutugame.websocket;

import com.horyu1234.kkutugame.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by horyu on 2017-03-24.
 */
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private Dispatcher dispatcher;
    private Logger logger;

    @Autowired
    public WebSocketHandler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        WSSessionFactory.webSocketSessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        dispatcher.mappingController(session, message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.close();

        WSSessionFactory.webSocketSessions.remove(session);
        logger.error("WebSocket 에서 오류가 발생하였습니다.", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WSSessionFactory.webSocketSessions.remove(session);
    }
}
