package com.horyu1234.kkutu_game.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by horyu on 2017-03-26.
 */
public class WSSessionFactory {
    public static List<WebSocketSession> webSocketSessions;

    static {
        webSocketSessions = new ArrayList<>();
    }
}
