package com.horyu1234.kkutugame.controller;

import com.google.gson.JsonElement;
import com.horyu1234.kkutugame.Error;
import com.horyu1234.kkutugame.LoginType;
import com.horyu1234.kkutugame.dao.LoginTypeDAO;
import com.horyu1234.kkutugame.request.FistBumpRequest;
import com.horyu1234.kkutugame.request.HandShakeRequest;
import com.horyu1234.kkutugame.request.RequestHandler;
import com.horyu1234.kkutugame.response.ErrorResponse;
import com.horyu1234.kkutugame.response.FistBumpResponse;
import com.horyu1234.kkutugame.response.HandShakeResponse;
import com.horyu1234.kkutugame.response.ResponseSender;
import com.horyu1234.kkutugame.websocket.WSSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by horyu on 2017-03-27.
 */
@EnableScheduling
@Controller
public class HandShakeController {
    private ResponseSender responseSender;
    private LoginTypeDAO loginTypeDAO;
    private Map<String, String> fistBumps;
    private Map<String, Long> fistBumpTimes;

    @Autowired
    public HandShakeController(ResponseSender responseSender, LoginTypeDAO loginTypeDAO) {
        this.responseSender = responseSender;
        this.loginTypeDAO = loginTypeDAO;
        this.fistBumps = new HashMap<>();
        this.fistBumpTimes = new HashMap<>();
    }

    @RequestHandler
    public void onHandShake(WebSocketSession session, HandShakeRequest handShakeRequest) {
        HandShakeResponse handShakeResponse = new HandShakeResponse();

        for (LoginType loginType : loginTypeDAO.getLoginTypes()) {
            handShakeResponse.getLoginTypes().add(loginType);
        }

        JsonElement jsonElement = responseSender.getJsonElement(handShakeResponse);

        String json = responseSender.getResponseJson(handShakeResponse, jsonElement);
        if (json == null) {
            return;
        }

        responseSender.sendJsonText(session, json);

        fistBumps.put(session.getId(), jsonElement.toString());
        fistBumpTimes.put(session.getId(), System.currentTimeMillis());
    }

    @RequestHandler
    public void onFistBump(WebSocketSession session, FistBumpRequest fistBumpRequest) {
        if (!fistBumps.containsKey(session.getId())) {
            return;
        }

        String originHandShake = fistBumps.get(session.getId());

        System.out.println("originHandShake: " + originHandShake);
        System.out.println("fistBumpRequest.getHandShake(): " + fistBumpRequest.getHandShake());

        if (originHandShake.equals(fistBumpRequest.getHandShake())) {
            // 성공
            System.out.println("성공");

            responseSender.sendResponse(session, new FistBumpResponse());
        } else {
            // 실패
            responseSender.sendResponse(session, new ErrorResponse(Error.FIST_BUMP_FAIL));
        }

        fistBumps.remove(session.getId());
        fistBumpTimes.remove(session.getId());
    }

    @Scheduled(fixedDelay = 500L)
    public void checkFistBumpTimeOut() {
        for (Map.Entry<String, Long> fistBumpTime : fistBumpTimes.entrySet()) {
            String sessionId = fistBumpTime.getKey();
            long time = fistBumpTime.getValue();

            if (time + 3000 < System.currentTimeMillis()) {
                // 시간 초과
                fistBumps.remove(sessionId);
                fistBumpTimes.remove(sessionId);

                responseSender.sendResponse(
                        WSSessionFactory.getWebSocketSession(sessionId),
                        new ErrorResponse(Error.FIST_BUMP_TIME_OUT));
            }
        }
    }
}
