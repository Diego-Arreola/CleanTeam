package com.cleanteam.mandarinplayer.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.cleanteam.mandarinplayer.DTO.LobbyEvent;
import com.cleanteam.mandarinplayer.DTO.StartMatchRequest;
import com.cleanteam.mandarinplayer.Game.Game;
import com.cleanteam.mandarinplayer.Game.GameFactory;
import com.cleanteam.mandarinplayer.Game.GameFactoryRegistry;
import com.cleanteam.mandarinplayer.Game.GameType;
import com.cleanteam.mandarinplayer.Model.Match;
import com.cleanteam.mandarinplayer.Model.MatchStatus;
import com.cleanteam.mandarinplayer.Repository.MatchRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para MatchStateManager")
class MatchStateManagerTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private GameFactoryRegistry gameFactoryRegistry;

    @Mock
    private GameFactory gameFactory;

    @Mock
    private Game game;

    @InjectMocks
    private MatchStateManager matchStateManager;

    private Match testMatch;

    @BeforeEach
    void setUp() {
        testMatch = new Match();
        testMatch.setStatus(MatchStatus.CONFIGURING);
        testMatch.setRoomCode("TEST01");
        testMatch.setGameType(GameType.MEMORAMA);
        testMatch.setPlayers(new HashSet<>(java.util.List.of("Player1", "Player2")));
    }

    @Test
    @DisplayName("Debe registrar conexión de jugador")
    void testOnPlayerConnected() {
        when(matchRepository.findByRoomCode("TEST01")).thenReturn(Optional.of(testMatch));

        matchStateManager.onPlayerConnected("TEST01", "session1", "Player1");

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<LobbyEvent> eventCaptor = ArgumentCaptor.forClass(LobbyEvent.class);
        verify(messagingTemplate).convertAndSend(topicCaptor.capture(), eventCaptor.capture());

        assertEquals("/topic/matches/TEST01", topicCaptor.getValue());
        LobbyEvent event = eventCaptor.getValue();
        assertEquals("TEST01", event.getRoomCode());
        assertEquals(1, event.getPlayersCount());
    }

    @Test
    @DisplayName("Debe registrar múltiples conexiones en la misma sala")
    void testMultiplePlayersConnected() {
        when(matchRepository.findByRoomCode("TEST01")).thenReturn(Optional.of(testMatch));

        matchStateManager.onPlayerConnected("TEST01", "session1", "Player1");
        matchStateManager.onPlayerConnected("TEST01", "session2", "Player2");

        verify(messagingTemplate, times(2)).convertAndSend(anyString(), any(LobbyEvent.class));
    }

    @Test
    @DisplayName("Debe desconectar jugador correctamente")
    void testOnPlayerDisconnected() {
        when(matchRepository.findByRoomCode("TEST01")).thenReturn(Optional.of(testMatch));

        matchStateManager.onPlayerConnected("TEST01", "session1", "Player1");
        matchStateManager.onPlayerDisconnected("TEST01", "session1");

        verify(messagingTemplate, times(2)).convertAndSend(anyString(), any(LobbyEvent.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si sala no existe al desconectar")
    void testOnPlayerDisconnectedNonExistent() {
        when(matchRepository.findByRoomCode("INVALID")).thenReturn(Optional.empty());
        
        // Debe lanzar excepción porque la sala no existe
        assertThrows(IllegalArgumentException.class, () -> matchStateManager.onPlayerDisconnected("INVALID", "session1"));
    }

    @Test
    @DisplayName("Debe iniciar partida correctamente")
    void testStartMatch() {
        StartMatchRequest request = new StartMatchRequest();
        request.setRoomCode("TEST01");

        when(matchRepository.findByRoomCode("TEST01")).thenReturn(Optional.of(testMatch));
        when(gameFactoryRegistry.getFactory(GameType.MEMORAMA)).thenReturn(gameFactory);
        when(gameFactory.createGame(testMatch)).thenReturn(game);
        when(matchRepository.save(any(Match.class))).thenReturn(testMatch);

        Match result = matchStateManager.start(request);

        assertNotNull(result);
        assertEquals(MatchStatus.IN_PROGRESS, testMatch.getStatus());
        verify(game, times(1)).start();
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si partida no existe")
    void testStartMatchNotFound() {
        StartMatchRequest request = new StartMatchRequest();
        request.setRoomCode("INVALID");

        when(matchRepository.findByRoomCode("INVALID")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> matchStateManager.start(request));
    }

    @Test
    @DisplayName("Debe lanzar excepción si no hay jugadores en la partida")
    void testStartMatchNoPlayers() {
        StartMatchRequest request = new StartMatchRequest();
        request.setRoomCode("TEST01");
        testMatch.setPlayers(new HashSet<>()); // Sin jugadores

        when(matchRepository.findByRoomCode("TEST01")).thenReturn(Optional.of(testMatch));

        assertThrows(IllegalStateException.class, () -> matchStateManager.start(request));
    }

    @Test
    @DisplayName("Debe publicar lobby con estado correcto")
    void testPublishLobbyByRoom() {
        when(matchRepository.findByRoomCode("TEST01")).thenReturn(Optional.of(testMatch));

        matchStateManager.onPlayerConnected("TEST01", "session1", "Player1");

        ArgumentCaptor<LobbyEvent> eventCaptor = ArgumentCaptor.forClass(LobbyEvent.class);
        verify(messagingTemplate).convertAndSend(anyString(), eventCaptor.capture());

        LobbyEvent event = eventCaptor.getValue();
        assertEquals("TEST01", event.getRoomCode());
        assertEquals(MatchStatus.CONFIGURING.name(), event.getStatus());
    }

    @Test
    @DisplayName("Debe actualizar estado de partida al iniciar")
    void testStartMatchStatusUpdate() {
        StartMatchRequest request = new StartMatchRequest();
        request.setRoomCode("TEST01");

        when(matchRepository.findByRoomCode("TEST01")).thenReturn(Optional.of(testMatch));
        when(gameFactoryRegistry.getFactory(GameType.MEMORAMA)).thenReturn(gameFactory);
        when(gameFactory.createGame(testMatch)).thenReturn(game);
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> {
            Match m = invocation.getArgument(0);
            return m;
        });

        matchStateManager.start(request);

        assertEquals(MatchStatus.IN_PROGRESS, testMatch.getStatus());
    }

    @Test
    @DisplayName("Debe usar GameFactoryRegistry para crear el juego")
    void testGameFactoryRegistryUsage() {
        StartMatchRequest request = new StartMatchRequest();
        request.setRoomCode("TEST01");

        when(matchRepository.findByRoomCode("TEST01")).thenReturn(Optional.of(testMatch));
        when(gameFactoryRegistry.getFactory(GameType.MEMORAMA)).thenReturn(gameFactory);
        when(gameFactory.createGame(testMatch)).thenReturn(game);
        when(matchRepository.save(any(Match.class))).thenReturn(testMatch);

        matchStateManager.start(request);

        verify(gameFactoryRegistry, times(1)).getFactory(GameType.MEMORAMA);
        verify(gameFactory, times(1)).createGame(testMatch);
    }

    @Test
    @DisplayName("Debe publicar lobby después de iniciar partida")
    void testPublishAfterStart() {
        StartMatchRequest request = new StartMatchRequest();
        request.setRoomCode("TEST01");

        when(matchRepository.findByRoomCode("TEST01")).thenReturn(Optional.of(testMatch));
        when(gameFactoryRegistry.getFactory(GameType.MEMORAMA)).thenReturn(gameFactory);
        when(gameFactory.createGame(testMatch)).thenReturn(game);
        when(matchRepository.save(any(Match.class))).thenReturn(testMatch);

        matchStateManager.start(request);

        ArgumentCaptor<LobbyEvent> eventCaptor = ArgumentCaptor.forClass(LobbyEvent.class);
        verify(messagingTemplate).convertAndSend(anyString(), eventCaptor.capture());

        LobbyEvent event = eventCaptor.getValue();
        assertEquals(MatchStatus.IN_PROGRESS.name(), event.getStatus());
    }
}
