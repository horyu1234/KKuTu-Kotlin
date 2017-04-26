package com.horyu1234.kkutugame.response;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.horyu1234.kkutugame.Error;
import com.horyu1234.kkutugame.LoginType;
import com.horyu1234.kkutugame.jsonserializer.ErrorSerializer;
import com.horyu1234.kkutugame.jsonserializer.LoginTypeSerializer;
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
    private GsonBuilder gsonBuilder;

    @Autowired
    public ResponseSender(LoginTypeSerializer loginTypeSerializer, ErrorSerializer errorSerializer) {
        this.logger = LoggerFactory.getLogger(this.getClass());

        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LoginType.class, loginTypeSerializer);
        gsonBuilder.registerTypeAdapter(Error.class, errorSerializer);
    }

    /**
     * 응답을 전송합니다.
     *
     * @param session 웹 소켓 세션
     * @param object  Response 객체
     */
    public void sendResponse(WebSocketSession session, Object object) {
        String json = getResponseJson(object, getJsonElement(object));
        if (json == null) {
            return;
        }

        sendJsonText(session, json);
    }

    /**
     * 응답을 전송합니다.
     *
     * @param session 웹 소켓 세션
     * @param json    Json 문자열
     */
    public void sendJsonText(WebSocketSession session, String json) {
        try {
            TextMessage message = new TextMessage(json);

            session.sendMessage(message);
        } catch (Exception e) {
            logger.error("WebSocket 로 응답을 전송하는 중 오류가 발생하였습니다.", e);
        }
    }

    /**
     * 응답 JsonElement 를 가져옵니다.
     *
     * @param object Response 객체
     * @return 응답 JsonElement
     */
    public JsonElement getJsonElement(Object object) {
        return gsonBuilder.create().toJsonTree(object);
    }

    /**
     * 최종 Json 문자열을 가져옵니다.
     *
     * @param object      Response 객체
     * @param jsonElement 응답 JsonElement
     * @return 최종 Json 문자열
     */
    public String getResponseJson(Object object, JsonElement jsonElement) {
        String objectSimpleClassName = object.getClass().getSimpleName();
        if (!objectSimpleClassName.endsWith("Response")) {
            logger.error("Response 클래스가 아닌 오브젝트를 응답으로 전송하려 했습니다.");
            return null;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", objectSimpleClassName.replace("Response", ""));
        jsonObject.add("value", jsonElement);

        return jsonObject.toString();
    }
}
