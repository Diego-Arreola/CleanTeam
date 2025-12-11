package com.cleanteam.mandarinplayer.infrastructure.gateways;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.cleanteam.mandarinplayer.domain.entities.Match;
import com.cleanteam.mandarinplayer.domain.entities.MatchStatus;
import com.cleanteam.mandarinplayer.domain.interfaces.MatchRepository;
import com.cleanteam.mandarinplayer.infrastructure.dto.LobbyEvent;
import com.cleanteam.mandarinplayer.infrastructure.gateways.MatchStateManager;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para MatchStateManager")
class MatchStateManagerTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private MatchStateManager matchStateManager;

    private Match testMatch;

    @BeforeEach
    void setUp() {
        testMatch = new Match();
        testMatch.setStatus(MatchStatus.CONFIGURING);
        testMatch.setRoomCode("TEST01");
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
        // Al conectar uno, debería haber al menos 1 en el contador interno del manager
        // Nota: El mock del repo devuelve un match con 2 jugadores ya, pero el evento usa el contador de sesiones
        // que empieza en 0 y sube a 1 con esta llamada.
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
        // Necesitamos conectar primero para tener algo que desconectar del Map interno
        when(matchRepository.findByRoomCode("TEST01")).thenReturn(Optional.of(testMatch));

        matchStateManager.onPlayerConnected("TEST01", "session1", "Player1");
        matchStateManager.onPlayerDisconnected("TEST01", "session1");

        // Se llama 1 vez al conectar y 1 vez al desconectar
        verify(messagingTemplate, times(2)).convertAndSend(anyString(), any(LobbyEvent.class));
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
    @DisplayName("Debe registrar múltiples desconexiones en la misma sala")
    void testMultiplePlayersDisconnected() {
        when(matchRepository.findByRoomCode("TEST01")).thenReturn(Optional.of(testMatch));

        matchStateManager.onPlayerConnected("TEST01", "session1", "Player1");
        matchStateManager.onPlayerConnected("TEST01", "session2", "Player2");
        matchStateManager.onPlayerDisconnected("TEST01", "session1");
        matchStateManager.onPlayerDisconnected("TEST01", "session2");

        // 2 connect + 2 disconnect = 4 updates
        verify(messagingTemplate, atLeast(4)).convertAndSend(anyString(), any(LobbyEvent.class));
    }

    @Test
    @DisplayName("Debe contar correctamente conexiones activas en publishLobby")
    void testPublishLobbyCountsConnections() {
        when(matchRepository.findByRoomCode("TEST01")).thenReturn(Optional.of(testMatch));

        matchStateManager.onPlayerConnected("TEST01", "session1", "Player1");
        matchStateManager.onPlayerConnected("TEST01", "session2", "Player2");

        ArgumentCaptor<LobbyEvent> eventCaptor = ArgumentCaptor.forClass(LobbyEvent.class);
        verify(messagingTemplate, atLeast(2)).convertAndSend(anyString(), eventCaptor.capture());

        // El último evento capturado debería reflejar 2 conexiones
        LobbyEvent event = eventCaptor.getValue();
        assertEquals(2, event.getPlayersCount());
    }
}