package com.horyu1234.kkutugame.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.horyu1234.kkutugame.LoginType;
import com.horyu1234.kkutugame.LoginTypeSerializer;
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
    private Logger logger;
    private LoginTypeSerializer loginTypeSerializer;

    @Autowired
    public ResponseSender(LoginTypeSerializer loginTypeSerializer) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.loginTypeSerializer = loginTypeSerializer;
    }

    public void sendResponse(WebSocketSession session, Object object) {
        JsonObject jsonObject = getJsonObject(object);
        if (jsonObject == null) {
            return;
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LoginType.class, loginTypeSerializer);

        Gson objectGson = gsonBuilder.create();
        jsonObject.add("value", objectGson.toJsonTree(object));

        sendResponse(session, jsonObject.toString());
    }

    private JsonObject getJsonObject(Object object) {
        String objectSimpleClassName = object.getClass().getSimpleName();
        if (!objectSimpleClassName.endsWith("Response")) {
            logger.error("Response 클래스가 아닌 오브젝트를 응답으로 전송하려 했습니다.");
            return null;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", objectSimpleClassName.replace("Response", ""));

        return jsonObject;
    }

    private void sendResponse(WebSocketSession session, String json) {
        try {
            TextMessage message = new TextMessage(json);

            session.sendMessage(message);
        } catch (Exception e) {
            logger.error("WebSocket 로 응답을 전송하는 중 오류가 발생하였습니다.", e);
        }
    }
}
