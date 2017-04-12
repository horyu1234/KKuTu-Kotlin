package com.horyu1234.kkutugame.controller;

import com.horyu1234.kkutugame.Channel;
import com.horyu1234.kkutugame.LoginType;
import com.horyu1234.kkutugame.request.FistBumpRequest;
import com.horyu1234.kkutugame.request.RequestHandler;
import com.horyu1234.kkutugame.response.HandShakeResponse;
import com.horyu1234.kkutugame.response.ResponseSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import java.util.Arrays;

/**
 * Created by horyu on 2017-03-27.
 */
@Controller
public class HandShakeController {
    private ResponseSender responseSender;

    @Autowired
    public HandShakeController(ResponseSender responseSender) {
        this.responseSender = responseSender;
    }

    @RequestHandler
    public void onHandShake(WebSocketSession webSocketSession) {
        Channel testChannel = new Channel();
        testChannel.setName("테스트 채널");
        testChannel.setLoginTypes(Arrays.asList(LoginType.GUEST, LoginType.NAVER));
        testChannel.setCurrentPlayer(77);
        testChannel.setMaxPlayers(2000);

        HandShakeResponse handShakeResponse = new HandShakeResponse();
        handShakeResponse.setChannels(Arrays.asList(testChannel));

        responseSender.sendResponse(webSocketSession, handShakeResponse);
    }

    @RequestHandler
    public void onFistBump(WebSocketSession webSocketSession, FistBumpRequest fistBumpRequest) {
        String handShake = fistBumpRequest.getHandShake();

        responseSender.sendResponse(webSocketSession, new FistBumpRequest());
    }
}
