package com.horyu1234.kkutugame.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by horyu on 2017-03-24.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private WebSocketHandler webSocketHandler;
    private HandShakeHandler handShakeHandler;

    public WebSocketConfig(WebSocketHandler webSocketHandler, HandShakeHandler handShakeHandler) {
        this.webSocketHandler = webSocketHandler;
        this.handShakeHandler = handShakeHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/*")
                .setAllowedOrigins("*") // TODO: 개발이 끝나면 보안상 웹서버 등 주소로 변경 필요
                .setHandshakeHandler(handShakeHandler);
    }
}
