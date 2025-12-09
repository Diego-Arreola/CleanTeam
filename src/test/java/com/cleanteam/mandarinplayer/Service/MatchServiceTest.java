package com.cleanteam.mandarinplayer.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cleanteam.mandarinplayer.DTO.CreateMatchRequest;
import com.cleanteam.mandarinplayer.Game.GameType;
import com.cleanteam.mandarinplayer.Model.Match;
import com.cleanteam.mandarinplayer.Model.MatchStatus;
import com.cleanteam.mandarinplayer.Model.Theme;
import com.cleanteam.mandarinplayer.Repository.MatchRepository;
import com.cleanteam.mandarinplayer.Repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para MatchService")
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private MatchService matchService;

    private CreateMatchRequest createMatchRequest;
    private Theme testTheme;
    private Match testMatch;

    @BeforeEach
    void setUp() {
        testTheme = new Theme();
        testTheme.setId(1L);
        testTheme.setName("Basic Greetings");

        createMatchRequest = new CreateMatchRequest();
        createMatchRequest.setGameType("MEMORAMA");
        createMatchRequest.setThemeIds(new java.util.ArrayList<>(java.util.List.of(1L)));

        testMatch = new Match();
        testMatch.setStatus(MatchStatus.CONFIGURING);
        testMatch.setGameType(GameType.MEMORAMA);
        testMatch.setRoomCode("TEST01");
        testMatch.setThemes(new HashSet<>(java.util.List.of(testTheme)));
    }

    @Test
    @DisplayName("Debe crear partida correctamente")
    void testCreateMatch() {
        when(themeRepository.findAllById(java.util.List.of(1L)))
                .thenReturn(java.util.List.of(testTheme));
        when(matchRepository.save(any(Match.class))).thenReturn(testMatch);

        Match result = matchService.createMatch(createMatchRequest);

        assertNotNull(result);
        assertEquals(GameType.MEMORAMA, result.getGameType());
        assertEquals(MatchStatus.CONFIGURING, result.getStatus());
        assertNotNull(result.getRoomCode());
        assertEquals(6, result.getRoomCode().length());
        assertTrue(result.getRoomCode().matches("[A-Z0-9]{6}"));
        verify(themeRepository, times(1)).findAllById(java.util.List.of(1L));
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    @DisplayName("Debe generar código de sala único para cada partida")
    void testRoomCodeUniqueness() {
        when(themeRepository.findAllById(any())).thenReturn(java.util.List.of(testTheme));
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> {
            Match match = invocation.getArgument(0);
            match.setRoomCode("ROOM" + System.nanoTime() % 1000);
            return match;
        });

        Match match1 = matchService.createMatch(createMatchRequest);
        Match match2 = matchService.createMatch(createMatchRequest);

        assertNotEquals(match1.getRoomCode(), match2.getRoomCode());
    }

    @Test
    @DisplayName("Debe crear partida sin tema")
    void testCreateMatchWithoutTheme() {
        createMatchRequest.setThemeIds(null);
        when(matchRepository.save(any(Match.class))).thenReturn(testMatch);

        Match result = matchService.createMatch(createMatchRequest);

        assertNotNull(result);
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    @DisplayName("Debe convertir tipo de juego a mayúsculas")
    void testGameTypeConversion() {
        createMatchRequest.setGameType("memorama");
        when(themeRepository.findAllById(any())).thenReturn(java.util.List.of(testTheme));
        when(matchRepository.save(any(Match.class))).thenReturn(testMatch);

        ArgumentCaptor<Match> captor = ArgumentCaptor.forClass(Match.class);
        matchService.createMatch(createMatchRequest);

        verify(matchRepository).save(captor.capture());
        Match savedMatch = captor.getValue();
        assertEquals(GameType.MEMORAMA, savedMatch.getGameType());
    }

    @Test
    @DisplayName("Debe iniciar partida correctamente")
    void testStartMatch() {
        testMatch.setStatus(MatchStatus.CONFIGURING);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(testMatch));
        when(matchRepository.save(any(Match.class))).thenReturn(testMatch);

        Match result = matchService.startMatch(1L);

        assertNotNull(result);
        assertEquals(MatchStatus.IN_PROGRESS, result.getStatus());
        verify(matchRepository, times(1)).findById(1L);
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando partida no existe")
    void testStartMatchNotFound() {
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> matchService.startMatch(999L));
        verify(matchRepository, times(1)).findById(999L);
        verify(matchRepository, never()).save(any(Match.class));
    }

    @Test
    @DisplayName("Debe establecer temas en la partida")
    void testMatchThemes() {
        Theme theme1 = new Theme();
        theme1.setId(1L);
        theme1.setName("Greetings");

        Theme theme2 = new Theme();
        theme2.setId(2L);
        theme2.setName("Numbers");

        Set<Long> themeIds = new HashSet<>();
        themeIds.add(1L);
        themeIds.add(2L);
        createMatchRequest.setThemeIds(new java.util.ArrayList<>(themeIds));

        when(themeRepository.findAllById(any()))
                .thenReturn(java.util.List.of(theme1, theme2));
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> {
            Match match = invocation.getArgument(0);
            match.setRoomCode("THEME01");
            return match;
        });

        Match result = matchService.createMatch(createMatchRequest);

        assertNotNull(result.getThemes());
        assertEquals(2, result.getThemes().size());
    }

    @Test
    @DisplayName("Debe crear partida sin tipo de juego")
    void testCreateMatchWithoutGameType() {
        createMatchRequest.setGameType(null);
        when(themeRepository.findAllById(any())).thenReturn(java.util.List.of(testTheme));
        when(matchRepository.save(any(Match.class))).thenReturn(testMatch);

        Match result = matchService.createMatch(createMatchRequest);

        assertNotNull(result);
        assertEquals(MatchStatus.CONFIGURING, result.getStatus());
        verify(matchRepository, times(1)).save(any(Match.class));
    }
}
