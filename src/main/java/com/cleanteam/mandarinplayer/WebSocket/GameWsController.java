package com.cleanteam.mandarinplayer.websocket;

import com.cleanteam.mandarinplayer.dto.FlipCardRequest;
import com.cleanteam.mandarinplayer.model.Match;
import com.cleanteam.mandarinplayer.repository.MatchRepository; 
import com.cleanteam.mandarinplayer.service.MatchService;       

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal; 

@Controller
public class GameWsController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MatchService matchService;          
    private final MatchRepository matchRepository;    

    public GameWsController(SimpMessagingTemplate messagingTemplate,
                            MatchService matchService,
                            MatchRepository matchRepository) {
        this.messagingTemplate = messagingTemplate;
        this.matchService = matchService;
        this.matchRepository = matchRepository;
    }

    @MessageMapping("/matches/flip")
    public void flip(@Payload FlipCardRequest req, StompHeaderAccessor accessor) {
        // Validaciones básicas
        if (req == null || req.getRoomCode() == null) {
            return;
        }
        
        String sessionId = accessor != null ? accessor.getSessionId() : null;
        if (sessionId == null) {
            return;
        }

        try {
            // 1. Buscamos la partida usando el roomCode que viene del frontend
            Match match = matchRepository.findByRoomCode(req.getRoomCode())
                    .orElseThrow(() -> new RuntimeException("Partida no encontrada: " + req.getRoomCode()));

            // El servicio usará automáticamente la Estrategia correcta (Memorama, etc.)
            matchService.playTurn(match.getId(), req);

        } catch (Exception e) {
            // Manejo de errores (enviar mensaje privado al usuario)
            Principal user = accessor != null ? accessor.getUser() : null;
            if (user != null) {
                String principalName = user.getName();
                if (principalName != null) {
                    messagingTemplate.convertAndSendToUser(principalName, "/queue/errors", e.getMessage());
                }
            }
        }
    }
}