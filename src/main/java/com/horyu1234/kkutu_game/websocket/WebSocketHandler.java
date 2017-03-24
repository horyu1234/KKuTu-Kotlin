package com.horyu1234.kkutu_game.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by horyu on 2017-03-24.
 */
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private Set<WebSocketSession> webSocketSessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("새로운 연결");
        System.out.println("요청 URL: " + session.getUri().toString());
        System.out.println("아이피: " + session.getRemoteAddress().getHostName());

        for (WebSocketSession sess : webSocketSessions) {
            sess.sendMessage(new TextMessage("접속: " + session.getId()));
            sess.sendMessage(new TextMessage("현재 연결된 세션 수: " + webSocketSessions.size()));
        }

        webSocketSessions.add(session);

        System.out.println("현재 연결된 세션 수: " + webSocketSessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(session.getId() + "로 부터 \"" + message.getPayload() + "\" 를 수신");

        session.sendMessage(new TextMessage("ECHO Server: " + message.getPayload()));

        System.out.println("현재 연결된 세션 수: " + webSocketSessions.size());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        exception.printStackTrace();
        session.close();

        System.out.println("세션 에러: " + session.getId());

        webSocketSessions.remove(session);

        System.out.println("현재 연결된 세션 수: " + webSocketSessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("세션 종료: " + session.getId());

        webSocketSessions.remove(session);

        System.out.println("현재 연결된 세션 수: " + webSocketSessions.size());
    }
}
