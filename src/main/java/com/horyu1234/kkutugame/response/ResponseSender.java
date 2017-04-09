package com.horyu1234.kkutugame.response;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by horyu on 2017-03-26.
 */
@Component
public class ResponseSender {
    private Gson gson;
    private Logger logger;

    @Autowired
    public ResponseSender(Gson gson) {
        this.gson = gson;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public void sendResponse(WebSocketSession session, Object object) {
        try {
            String jsonStr = gson.toJson(object);
            TextMessage message = new TextMessage(jsonStr);

            session.sendMessage(message);
        } catch (Exception e) {
            logger.error("WebSocket 로 응답을 전송하는 중 오류가 발생하였습니다.", e);
        }
    }
}
