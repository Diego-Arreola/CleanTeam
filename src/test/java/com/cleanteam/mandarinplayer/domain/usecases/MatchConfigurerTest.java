package com.cleanteam.mandarinplayer.domain.usecases;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cleanteam.mandarinplayer.domain.entities.GameType;
import com.cleanteam.mandarinplayer.domain.entities.Match;
import com.cleanteam.mandarinplayer.domain.entities.MatchStatus;
import com.cleanteam.mandarinplayer.domain.entities.Theme;
import com.cleanteam.mandarinplayer.domain.interfaces.ThemeRepository;
import com.cleanteam.mandarinplayer.domain.usecases.MatchConfigurer;
import com.cleanteam.mandarinplayer.infrastructure.dto.CreateMatchRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para MatchConfigurer")
class MatchConfigurerTest {

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private MatchConfigurer matchConfigurer;

    private CreateMatchRequest createMatchRequest;
    private Theme testTheme;

    @BeforeEach
    void setUp() {
        testTheme = new Theme();
        testTheme.setId(1L);
        testTheme.setName("Basic Greetings");

        createMatchRequest = new CreateMatchRequest();
        createMatchRequest.setGameType("MEMORAMA");
        createMatchRequest.setThemeIds(new ArrayList<>(List.of(1L)));
    }

    @Test
    @DisplayName("Debe configurar partida correctamente")
    void testConfigure() {
        when(themeRepository.findAllById(List.of(1L)))
                .thenReturn(List.of(testTheme));

        Match result = matchConfigurer.configure(createMatchRequest);

        assertNotNull(result);
        assertEquals(GameType.MEMORAMA, result.getGameType());
        assertEquals(MatchStatus.CONFIGURING, result.getStatus());
        assertNotNull(result.getRoomCode());
        assertEquals(6, result.getRoomCode().length());
        assertTrue(result.getRoomCode().matches("[A-Z0-9]{6}"));
        assertNotNull(result.getCreatedAt());
    }

    @Test
    @DisplayName("Debe lanzar excepción si tipo de juego es null")
    void testConfigureWithNullGameType() {
        createMatchRequest.setGameType(null);

        assertThrows(IllegalArgumentException.class, () -> matchConfigurer.configure(createMatchRequest));
    }

    @Test
    @DisplayName("Debe lanzar excepción si tipo de juego es vacío")
    void testConfigureWithBlankGameType() {
        createMatchRequest.setGameType("   ");

        assertThrows(IllegalArgumentException.class, () -> matchConfigurer.configure(createMatchRequest));
    }

    @Test
    @DisplayName("Debe convertir tipo de juego a mayúsculas")
    void testGameTypeConversionToUpperCase() {
        createMatchRequest.setGameType("memorama");
        when(themeRepository.findAllById(List.of(1L)))
                .thenReturn(List.of(testTheme));

        Match result = matchConfigurer.configure(createMatchRequest);

        assertEquals(GameType.MEMORAMA, result.getGameType());
    }

    @Test
    @DisplayName("Debe configurar temas cuando están presentes")
    void testConfigureWithThemes() {
        Theme theme2 = new Theme();
        theme2.setId(2L);
        theme2.setName("Numbers");

        createMatchRequest.setThemeIds(new ArrayList<>(List.of(1L, 2L)));
        when(themeRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(testTheme, theme2));

        Match result = matchConfigurer.configure(createMatchRequest);

        assertNotNull(result.getThemes());
        assertEquals(2, result.getThemes().size());
    }

    @Test
    @DisplayName("Debe configurar partida sin temas")
    void testConfigureWithoutThemes() {
        createMatchRequest.setThemeIds(null);

        Match result = matchConfigurer.configure(createMatchRequest);

        assertNotNull(result);
        assertTrue(result.getThemes() == null || result.getThemes().isEmpty());
    }

    @Test
    @DisplayName("Debe generar código de sala único para cada partida")
    void testRoomCodeUniqueness() {
        when(themeRepository.findAllById(List.of(1L)))
                .thenReturn(List.of(testTheme));

        Match match1 = matchConfigurer.configure(createMatchRequest);
        Match match2 = matchConfigurer.configure(createMatchRequest);

        assertNotEquals(match1.getRoomCode(), match2.getRoomCode());
    }

    @Test
    @DisplayName("Debe lanzar excepción si tipo de juego no existe en enum")
    void testInvalidGameType() {
        createMatchRequest.setGameType("INVALID_GAME_TYPE");

        assertThrows(IllegalArgumentException.class, () -> matchConfigurer.configure(createMatchRequest));
    }

    @Test
    @DisplayName("Debe configurar con lista de temas vacía")
    void testConfigureWithEmptyThemeList() {
        createMatchRequest.setThemeIds(new ArrayList<>());

        Match result = matchConfigurer.configure(createMatchRequest);

        assertNotNull(result);
        assertTrue(result.getThemes() == null || result.getThemes().isEmpty());
    }
}
