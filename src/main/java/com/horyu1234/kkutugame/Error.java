package com.horyu1234.kkutugame;

/**
 * Created by horyu on 2017-04-26.
 */
public enum Error {
    FIST_BUMP_FAIL("통신 데이터에 문제가 발생하였습니다. 네트워크 상태를 확인해주세요."),
    FIST_BUMP_TIME_OUT("통신 시간이 초과되었습니다. 네트워크 상태를 확인해주세요.");

    private String message;

    Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
