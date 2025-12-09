// java
package com.cleanteam.mandarinplayer.Game;

import com.cleanteam.mandarinplayer.Model.Match;
import com.cleanteam.mandarinplayer.Service.MemoramaGameService;
import org.springframework.stereotype.Component;

@Component
public class MemoramaGameFactory implements GameFactory {

    private final MemoramaGameService memoramaGameService;

    public MemoramaGameFactory(MemoramaGameService memoramaGameService) {
        this.memoramaGameService = memoramaGameService;
    }

    @Override
    public Game createGame(Match match) {
        return new MemoramaGame(match, memoramaGameService);
    }


    public class MemoramaGame implements Game {

        private final Match match;
        private final MemoramaGameService service;

        public MemoramaGame(Match match, MemoramaGameService service) {
            this.match = match;
            this.service = service;
        }

        @Override
        public void start() {
            // Inicializa el estado del juego con datos del match
            service.initializeGame(match);
            // Si necesitas emitir eventos adicionales, hazlo dentro del servicio
        }
    }
}