package com.cleanteam.mandarinplayer.Game;

import com.cleanteam.mandarinplayer.Model.Match;

public interface GameFactory {
    Game createGame(Match match);
}
