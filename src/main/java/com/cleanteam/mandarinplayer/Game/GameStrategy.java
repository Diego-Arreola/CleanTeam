package com.cleanteam.mandarinplayer.game;

import com.cleanteam.mandarinplayer.dto.FlipCardRequest;
import com.cleanteam.mandarinplayer.model.Match;

public interface GameStrategy {
    // Nos dice qué juego es (MEMORAMA, WORD_GAME, etc.)
    GameType getGameType();

    // Ejecuta la lógica del turno
    void playTurn(Match match, FlipCardRequest request);
}