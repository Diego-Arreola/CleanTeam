package com.cleanteam.mandarinplayer.domain.usecases;

import com.cleanteam.mandarinplayer.domain.entities.GameType;
import com.cleanteam.mandarinplayer.domain.entities.Match;
import com.cleanteam.mandarinplayer.infrastructure.dto.FlipCardRequest;

public interface GameStrategy {
    // Nos dice qué juego es (MEMORAMA, WORD_GAME, etc.)
    GameType getGameType();

    // Ejecuta la lógica del turno
    void playTurn(Match match, FlipCardRequest request);
}