package com.cleanteam.mandarinplayer.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cleanteam.mandarinplayer.domain.entities.GameType;
import com.cleanteam.mandarinplayer.domain.entities.Match;
import com.cleanteam.mandarinplayer.domain.entities.MatchStatus;
import com.cleanteam.mandarinplayer.domain.entities.Theme;
import com.cleanteam.mandarinplayer.domain.entities.GameMode;
import com.cleanteam.mandarinplayer.domain.interfaces.GameModeRepository;
import com.cleanteam.mandarinplayer.domain.interfaces.MatchRepository;
import com.cleanteam.mandarinplayer.domain.interfaces.ThemeRepository;
import com.cleanteam.mandarinplayer.infrastructure.dto.CreateMatchRequest;
import com.cleanteam.mandarinplayer.infrastructure.dto.FlipCardRequest;
import com.cleanteam.mandarinplayer.infrastructure.dto.StartMatchRequest;
import com.cleanteam.mandarinplayer.infrastructure.gateways.MatchStateManager;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para MatchUseCase")
class MatchUseCaseTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private GameModeRepository gameModeRepository;

    @Mock
    private MatchConfigurer matchConfigurer;

    @Mock
    private MatchStateManager matchStateManager;

    @Mock
    private GameStrategy gameStrategy;

    private MatchUseCase matchUseCase;

    @BeforeEach
    void setUp() {
        List<GameStrategy> strategies = new ArrayList<>();
        strategies.add(gameStrategy);
        
        matchUseCase = new MatchUseCase(
            matchRepository,
            themeRepository,
            gameModeRepository,
            matchConfigurer,
            matchStateManager,
            strategies
        );
    }

    // ===== PRUEBAS DE INSTANCIACIÓN =====
    
    @Test
    @DisplayName("Debe crear instancia válida de MatchUseCase")
    void testCreateInstance() {
        assertNotNull(matchUseCase);
    }

    @Test
    @DisplayName("Debe inyectar dependencias correctamente")
    void testDependenciesInjection() {
        assertNotNull(matchRepository);
        assertNotNull(themeRepository);
        assertNotNull(gameModeRepository);
        assertNotNull(matchConfigurer);
        assertNotNull(matchStateManager);
    }

    @Test
    @DisplayName("createMatch debe configurar y guardar una partida")
    void testCreateMatch() {
        // Arrange
        CreateMatchRequest request = new CreateMatchRequest();
        request.setGameType("MEMORAMA");
        
        Match configuredMatch = new Match();
        configuredMatch.setRoomCode("TEST123");
        configuredMatch.setGameType(GameType.MEMORAMA);
        configuredMatch.setStatus(MatchStatus.CONFIGURING);
        configuredMatch.setCreatedAt(LocalDateTime.now());
        
        when(matchConfigurer.configure(request)).thenReturn(configuredMatch);
        when(matchRepository.save(configuredMatch)).thenReturn(configuredMatch);
        
        // Act
        Match result = matchUseCase.createMatch(request);
        
        // Assert
        assertNotNull(result);
        assertEquals("TEST123", result.getRoomCode());
        assertEquals(GameType.MEMORAMA, result.getGameType());
        verify(matchConfigurer, times(1)).configure(request);
        verify(matchRepository, times(1)).save(configuredMatch);
    }

    // ===== PRUEBAS DE startMatch =====
    
    @Test
    @DisplayName("startMatch debe lanzar excepción si la sala no existe")
    void testStartMatchRoomNotFound() {
        // Arrange
        StartMatchRequest request = new StartMatchRequest();
        request.setRoomCode("NONEXISTENT");
        
        when(matchRepository.findByRoomCode("NONEXISTENT")).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> matchUseCase.startMatch(request));
        verify(matchRepository, times(1)).findByRoomCode("NONEXISTENT");
    }
    
    @Test
    @DisplayName("startMatch debe lanzar excepción si no hay jugadores")
    void testStartMatchNoPlayers() {
        // Arrange
        StartMatchRequest request = new StartMatchRequest();
        request.setRoomCode("TEST123");
        
        Match match = new Match();
        match.setRoomCode("TEST123");
        match.setStatus(MatchStatus.CONFIGURING);
        match.setPlayers(new HashSet<>()); // Sin jugadores
        
        when(matchRepository.findByRoomCode("TEST123")).thenReturn(Optional.of(match));
        
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> matchUseCase.startMatch(request));
    }
    
    @Test
    @DisplayName("startMatch debe cambiar estado a IN_PROGRESS y notificar")
    void testStartMatchSuccess() {
        // Arrange
        StartMatchRequest request = new StartMatchRequest();
        request.setRoomCode("TEST123");
        
        Match match = new Match();
        match.setRoomCode("TEST123");
        match.setStatus(MatchStatus.CONFIGURING);
        match.setGameType(GameType.MEMORAMA);
        Set<String> players = new HashSet<>();
        players.add("Player1");
        match.setPlayers(players);
        
        when(matchRepository.findByRoomCode("TEST123")).thenReturn(Optional.of(match));
        when(matchRepository.save(match)).thenReturn(match);
        
        // Act
        Match result = matchUseCase.startMatch(request);
        
        // Assert
        assertNotNull(result);
        assertEquals(MatchStatus.IN_PROGRESS, result.getStatus());
        verify(matchRepository, times(1)).findByRoomCode("TEST123");
        verify(matchRepository, times(1)).save(match);
        verify(matchStateManager, times(1)).publishLobbyUpdate(match);
    }

    // ===== PRUEBAS DE getAllThemes =====
    
    @Test
    @DisplayName("getAllThemes debe retornar lista de temas")
    void testGetAllThemes() {
        // Arrange
        List<Theme> themes = new ArrayList<>();
        Theme theme1 = new Theme();
        theme1.setName("Animales");
        themes.add(theme1);
        
        when(themeRepository.findAll()).thenReturn(themes);
        
        // Act
        List<?> result = matchUseCase.getAllThemes();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(themeRepository, times(1)).findAll();
    }

    // ===== PRUEBAS DE getAllGameModes =====
    
    @Test
    @DisplayName("getAllGameModes debe retornar lista de modos de juego")
    void testGetAllGameModes() {
        // Arrange
        List<GameMode> gameModes = new ArrayList<>();
        GameMode mode = new GameMode();
        gameModes.add(mode);
        
        when(gameModeRepository.findAll()).thenReturn(gameModes);
        
        // Act
        List<?> result = matchUseCase.getAllGameModes();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(gameModeRepository, times(1)).findAll();
    }

    // ===== PRUEBAS DE playTurn =====
    
    @Test
    @DisplayName("playTurn debe lanzar excepción si la partida no existe")
    void testPlayTurnMatchNotFound() {
        // Arrange
        FlipCardRequest request = new FlipCardRequest();
        
        when(matchRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> matchUseCase.playTurn(1L, request));
    }
    
    @Test
    @DisplayName("playTurn debe lanzar excepción si no hay estrategia para el juego")
    void testPlayTurnNoStrategy() {
        // Arrange
        FlipCardRequest request = new FlipCardRequest();
        request.setCardPosition(0);
        
        Match match = new Match();
        match.setStatus(MatchStatus.IN_PROGRESS);
        match.setGameType(GameType.QUIZ); // Tipo no en la lista de estrategias (solo MEMORAMA)
        
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> matchUseCase.playTurn(1L, request));
    }
    
}

