package com.horyu1234.kkutu_game.response;

import com.horyu1234.kkutu_game.Channel;

import java.util.List;

/**
 * Created by horyu on 2017-03-27.
 */
public class HandShakeResponse {
    private List<Channel> channels;

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }
}
