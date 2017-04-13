package com.horyu1234.kkutugame.controller;

import com.horyu1234.kkutugame.LoginType;
import com.horyu1234.kkutugame.dao.LoginTypeDAO;
import com.horyu1234.kkutugame.request.RequestHandler;
import com.horyu1234.kkutugame.response.HandShakeResponse;
import com.horyu1234.kkutugame.response.ResponseSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by horyu on 2017-03-27.
 */
@Controller
public class HandShakeController {
    private ResponseSender responseSender;
    private LoginTypeDAO loginTypeDAO;

    @Autowired
    public HandShakeController(ResponseSender responseSender, LoginTypeDAO loginTypeDAO) {
        this.responseSender = responseSender;
        this.loginTypeDAO = loginTypeDAO;
    }

    @RequestHandler
    public void onHandShake(WebSocketSession webSocketSession) {
        HandShakeResponse handShakeResponse = new HandShakeResponse();
        for (LoginType loginType : loginTypeDAO.getLoginTypes()) {
            handShakeResponse.getLoginTypes().add(loginType);
        }

        responseSender.sendResponse(webSocketSession, handShakeResponse);
    }
}
