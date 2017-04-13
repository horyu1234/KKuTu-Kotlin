package com.horyu1234.kkutugame.response;

import com.horyu1234.kkutugame.LoginType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by horyu on 2017-03-27.
 */
public class HandShakeResponse {
    private List<LoginType> loginTypes;

    public HandShakeResponse() {
        loginTypes = new ArrayList<>();
    }

    public List<LoginType> getLoginTypes() {
        return loginTypes;
    }
}
