package com.horyu1234.kkutugame;

import java.util.List;

/**
 * Created by horyu on 2017-03-27.
 */
public class Channel {
    private String name;
    private List<LoginType> loginTypes;
    private int currentPlayer;
    private int maxPlayers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LoginType> getLoginTypes() {
        return loginTypes;
    }

    public void setLoginTypes(List<LoginType> loginTypes) {
        this.loginTypes = loginTypes;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}
