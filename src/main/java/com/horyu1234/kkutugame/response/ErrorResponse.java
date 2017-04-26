package com.horyu1234.kkutugame.response;

import com.horyu1234.kkutugame.Error;

/**
 * Created by horyu on 2017-04-26.
 */
public class ErrorResponse {
    private Error error;

    public ErrorResponse(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
