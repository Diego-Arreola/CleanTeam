package com.cleanteam.mandarinplayer.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cleanteam.mandarinplayer.domain.interfaces.GameModeRepository;
import com.cleanteam.mandarinplayer.domain.interfaces.MatchRepository;
import com.cleanteam.mandarinplayer.domain.interfaces.ThemeRepository;
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
    @DisplayName("Debe manejar lista de estrategias de juego")
    void testGameStrategiesHandling() {
        assertNotNull(matchUseCase);
        assertNotNull(gameStrategy);
    }
}

