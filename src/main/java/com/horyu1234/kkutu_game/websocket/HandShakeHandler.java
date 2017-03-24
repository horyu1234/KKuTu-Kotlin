package com.horyu1234.kkutu_game.websocket;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by horyu on 2017-03-24.
 */
@Component
public class HandShakeHandler extends DefaultHandshakeHandler {
    @Override
    protected void handleInvalidUpgradeHeader(ServerHttpRequest request, ServerHttpResponse response) throws IOException {
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.getBody().write("400 Bad Request".getBytes(Charset.forName("UTF-8")));
    }
}
