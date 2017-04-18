package com.horyu1234.kkutugame.controller;

import com.google.gson.JsonElement;
import com.horyu1234.kkutugame.LoginType;
import com.horyu1234.kkutugame.dao.LoginTypeDAO;
import com.horyu1234.kkutugame.request.FistBumpRequest;
import com.horyu1234.kkutugame.request.HandShakeRequest;
import com.horyu1234.kkutugame.request.RequestHandler;
import com.horyu1234.kkutugame.response.FistBumpResponse;
import com.horyu1234.kkutugame.response.HandShakeResponse;
import com.horyu1234.kkutugame.response.ResponseSender;
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
    private Map<String, String> fistBump;
    private Map<String, Long> fistBumpTime;

    @Autowired
    public HandShakeController(ResponseSender responseSender, LoginTypeDAO loginTypeDAO) {
        this.responseSender = responseSender;
        this.loginTypeDAO = loginTypeDAO;
        this.fistBump = new HashMap<>();
        this.fistBumpTime = new HashMap<>();
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

        fistBump.put(session.getId(), jsonElement.toString());
        fistBumpTime.put(session.getId(), System.currentTimeMillis());
    }

    @RequestHandler
    public void onFistBump(WebSocketSession session, FistBumpRequest fistBumpRequest) {
        if (!fistBump.containsKey(session.getId())) {
            return;
        }

        String originHandShake = fistBump.get(session.getId());

        System.out.println("originHandShake: " + originHandShake);
        System.out.println("fistBumpRequest.getHandShake(): " + fistBumpRequest.getHandShake());

        if (originHandShake.equals(fistBumpRequest.getHandShake())) {
            // 성공
            System.out.println("성공");

            responseSender.sendResponse(session, new FistBumpResponse());
        } else {
            // 실패
            System.out.println("실패");
        }

        fistBump.remove(session.getId());
        fistBumpTime.remove(session.getId());
    }

    @Scheduled(fixedDelay = 500L)
    public void checkFistBumpTimeOut() {
        for (String session : fistBumpTime.keySet()) {
            long time = fistBumpTime.get(session);
            if (time + 3000 < System.currentTimeMillis()) {
                // 시간 초과
                System.out.println("시간 초과");

                fistBump.remove(session);
                fistBumpTime.remove(session);
            }
        }
    }
}
