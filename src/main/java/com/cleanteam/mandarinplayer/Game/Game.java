package com.cleanteam.mandarinplayer.game;

import com.cleanteam.mandarinplayer.dto.FlipCardRequest;

public interface Game {
    void start(); // inicializa rondas, carga palabras del/los temas, etc.
    void onFlip(FlipCardRequest req);
}

