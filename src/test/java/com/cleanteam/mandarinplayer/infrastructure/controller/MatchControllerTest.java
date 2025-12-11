package com.cleanteam.mandarinplayer.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.cleanteam.mandarinplayer.domain.entities.Match;
import com.cleanteam.mandarinplayer.domain.usecases.MatchUseCase;
import com.cleanteam.mandarinplayer.infrastructure.dto.CreateMatchRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para MatchController")
class MatchControllerTest {

    @Mock
    private MatchUseCase matchUseCase;

    private MatchController matchController;

    @BeforeEach
    void setUp() {
        matchController = new MatchController(matchUseCase);
    }

    @Test
    @DisplayName("Debe crear instancia del controlador")
    void testCreateController() {
        assertNotNull(matchController);
    }

    @Test
    @DisplayName("Debe obtener temas")
    void testGetThemes() {
        when(matchUseCase.getAllThemes()).thenReturn(List.of());
        
        ResponseEntity<?> response = matchController.getThemes();
        
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    @DisplayName("Debe obtener modos de juego")
    void testGetGameModes() {
        when(matchUseCase.getAllGameModes()).thenReturn(List.of());
        
        ResponseEntity<?> response = matchController.getGameModes();
        
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    @DisplayName("Debe crear un partido")
    void testCreateMatch() {
        CreateMatchRequest request = new CreateMatchRequest();
        Match match = new Match();
        
        when(matchUseCase.createMatch(request)).thenReturn(match);
        
        ResponseEntity<Match> response = matchController.createMatch(request);
        
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}

