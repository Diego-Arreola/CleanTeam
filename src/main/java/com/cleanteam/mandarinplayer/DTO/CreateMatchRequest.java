// java
package com.cleanteam.mandarinplayer.DTO;

import com.cleanteam.mandarinplayer.Game.GameType;

import java.util.List;

public class CreateMatchRequest {
    private String gameType; // valor del enum, p.ej. "MEMORAMA"
    private List<Long> themeIds;

    public String getGameType() { return gameType; }
    public void setGameType(String gameType) { this.gameType = gameType; }

    public List<Long> getThemeIds() { return themeIds; }
    public void setThemeIds(List<Long> themeIds) { this.themeIds = themeIds; }
}