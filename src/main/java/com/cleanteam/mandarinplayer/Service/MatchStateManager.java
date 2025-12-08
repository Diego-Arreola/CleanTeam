// java
package com.cleanteam.mandarinplayer.Service;

import com.cleanteam.mandarinplayer.DTO.LobbyEvent;
import com.cleanteam.mandarinplayer.DTO.StartMatchRequest;
import com.cleanteam.mandarinplayer.Game.Game;
import com.cleanteam.mandarinplayer.Game.GameFactoryRegistry;
import com.cleanteam.mandarinplayer.Model.Match;
import com.cleanteam.mandarinplayer.Model.MatchStatus;
import com.cleanteam.mandarinplayer.Repository.MatchRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MatchStateManager {

    private final MatchRepository matchRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final GameFactoryRegistry gameFactoryRegistry;

    // Contador de conexiones por roomCode basado en sessionIds
    private final ConcurrentHashMap<String, Set<String>> roomConnections = new ConcurrentHashMap<>();

    public MatchStateManager(MatchRepository matchRepository,
                             SimpMessagingTemplate messagingTemplate,
                             GameFactoryRegistry gameFactoryRegistry) {
        this.matchRepository = matchRepository;
        this.messagingTemplate = messagingTemplate;
        this.gameFactoryRegistry = gameFactoryRegistry;
    }

    // Evento desde WebSocket: incrementa contador y publica lobby
    public void onPlayerConnected(String roomCode, String sessionId, String nickname) {
        roomConnections.computeIfAbsent(roomCode, rc -> ConcurrentHashMap.newKeySet())
                       .add(sessionId);
        publishLobbyByRoom(roomCode);
    }

    // Evento desde WebSocket: decrementa contador y publica lobby
    public void onPlayerDisconnected(String roomCode, String sessionId) {
        Set<String> sessions = roomConnections.get(roomCode);
        if (sessions != null) {
            sessions.remove(sessionId);
            if (sessions.isEmpty()) {
                roomConnections.remove(roomCode);
            }
        }
        publishLobbyByRoom(roomCode);
    }

    @Transactional
    public Match start(StartMatchRequest req) {
        Match match = matchRepository.findByRoomCode(req.getRoomCode())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        if (match.getPlayers().isEmpty()) {
            throw new IllegalStateException("At least one player required");
        }
        match.setStatus(MatchStatus.IN_PROGRESS);

        Game game = gameFactoryRegistry.getFactory(match.getGameType()).createGame(match);
        game.start();

        Match saved = matchRepository.save(match);
        publishLobby(saved);
        return saved;
    }

    private void publishLobby(Match match) {
        int connected = roomConnections.getOrDefault(match.getRoomCode(), ConcurrentHashMap.newKeySet()).size();
        LobbyEvent event = new LobbyEvent(
                match.getRoomCode(),
                connected, // contador de conectados
                match.getStatus().name()
        );
        messagingTemplate.convertAndSend("/topic/matches/" + match.getRoomCode(), event);
    }

    private void publishLobbyByRoom(String roomCode) {
        Match match = matchRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        publishLobby(match);
    }
}