// java
package com.cleanteam.mandarinplayer.websocket;

import com.cleanteam.mandarinplayer.dto.JoinMatchRequest;
import com.cleanteam.mandarinplayer.service.MatchStateManager;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyWsController {

    private final MatchStateManager matchStateManager;

    public LobbyWsController(MatchStateManager matchStateManager) {
        this.matchStateManager = matchStateManager;
    }

    @MessageMapping("/matches/join")
    public void join(@Payload JoinMatchRequest req, StompHeaderAccessor accessor) {
        // Obtener el sessionId desde el accessor
        final String sessionId = (accessor != null) ? accessor.getSessionId() : null;

        if (req == null || req.getRoomCode() == null || req.getNickname() == null || sessionId == null) {
            return;
        }

        // Persistir en atributos de sesión para recuperar en DISCONNECT
        accessor.getSessionAttributes().put("roomCode", req.getRoomCode());
        accessor.getSessionAttributes().put("nickname", req.getNickname());

        matchStateManager.onPlayerConnected(req.getRoomCode(), sessionId, req.getNickname());
    }
}