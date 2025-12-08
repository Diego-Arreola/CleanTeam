package com.cleanteam.mandarinplayer.Controller;


import com.cleanteam.mandarinplayer.DTO.FlipCardRequest;
import com.cleanteam.mandarinplayer.Service.MemoramaGameService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class MemoramaGameController {

    private final MemoramaGameService gameService;

    public MemoramaGameController(MemoramaGameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/game/flip")
    public void flipCard(@Payload FlipCardRequest request) {
        gameService.flipCard(request);
    }
}

