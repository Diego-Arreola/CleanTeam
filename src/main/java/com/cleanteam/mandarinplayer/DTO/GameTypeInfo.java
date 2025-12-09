package com.cleanteam.mandarinplayer.dto;

import com.cleanteam.mandarinplayer.game.GameType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameTypeInfo {
    private GameType type;
    private String displayName;
    private String description;
    private int minPlayers;
    private int maxPlayers;
}

