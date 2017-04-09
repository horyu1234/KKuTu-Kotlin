package com.horyu1234.kkutugame.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by horyu on 2017-03-26.
 */
public class WSSessionFactory {
    private static List<WebSocketSession> webSocketSessions;

    static {
        webSocketSessions = new ArrayList<>();
    }

    private WSSessionFactory() {
        throw new IllegalAccessError("Factory 클래스는 초기화시킬 수 없습니다.");
    }

    public static List<WebSocketSession> getWebSocketSessions() {
        return webSocketSessions;
    }
}
