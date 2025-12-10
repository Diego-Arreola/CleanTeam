package com.cleanteam.mandarinplayer.infrastructure.gateways;

import com.cleanteam.mandarinplayer.domain.entities.Match;
import com.cleanteam.mandarinplayer.domain.interfaces.MatchRepository;
import com.cleanteam.mandarinplayer.infrastructure.dto.LobbyEvent;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MatchStateManager:
 * su ÚNICA responsabilidad es gestionar quién está conectado al Lobby (WebSocket).
 */
@Service
public class MatchStateManager {

    private final MatchRepository matchRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // Contador de conexiones por roomCode
    private final ConcurrentHashMap<String, Set<String>> roomConnections = new ConcurrentHashMap<>();

    public MatchStateManager(MatchRepository matchRepository,
                             SimpMessagingTemplate messagingTemplate) {
        this.matchRepository = matchRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // --- Gestión de Conexiones (Lobby) ---

    @Transactional
    public void onPlayerConnected(String roomCode, String sessionId, String nickname) {
        // 1. Registrar la sesión
        roomConnections.computeIfAbsent(roomCode, rc -> ConcurrentHashMap.newKeySet())
                .add(sessionId);

        // 2. Agregar jugador a la DB si tiene nickname
        Match match = matchRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomCode));
        
        if (nickname != null && !nickname.isBlank()) {
            match.getPlayers().add(nickname);
            matchRepository.save(match);
        }

        // 3. Avisar a todos en el lobby
        publishLobbyUpdate(match);
    }

    public void onPlayerDisconnected(String roomCode, String sessionId) {
        Set<String> sessions = roomConnections.get(roomCode);
        if (sessions != null) {
            sessions.remove(sessionId);
            if (sessions.isEmpty()) {
                roomConnections.remove(roomCode);
            }
        }
        // Intentamos buscar el match para publicar la actualización
        matchRepository.findByRoomCode(roomCode).ifPresent(this::publishLobbyUpdate);
    }

    // --- Métodos de Ayuda ---

    public void publishLobbyUpdate(Match match) {
        int connectedCount = roomConnections.getOrDefault(match.getRoomCode(), ConcurrentHashMap.newKeySet()).size();
        
        LobbyEvent event = new LobbyEvent(
                match.getRoomCode(),
                connectedCount,
                match.getStatus().name()
        );
        
        // Enviar evento al canal del lobby
        messagingTemplate.convertAndSend("/topic/matches/" + match.getRoomCode(), event);
    }
}