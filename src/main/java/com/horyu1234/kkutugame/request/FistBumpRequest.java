package com.horyu1234.kkutugame.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by horyu on 2017-03-27.
 */
public class FistBumpRequest {
    @SerializedName("HandShake")
    private String handShake;

    public String getHandShake() {
        return handShake;
    }
}
