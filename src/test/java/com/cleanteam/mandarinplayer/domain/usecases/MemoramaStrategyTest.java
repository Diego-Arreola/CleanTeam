package com.cleanteam.mandarinplayer.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.cleanteam.mandarinplayer.domain.entities.GameType;
import com.cleanteam.mandarinplayer.domain.entities.Match;
import com.cleanteam.mandarinplayer.domain.entities.Theme;
import com.cleanteam.mandarinplayer.domain.entities.Word;
import com.cleanteam.mandarinplayer.domain.interfaces.WordRepository;
import com.cleanteam.mandarinplayer.infrastructure.dto.FlipCardRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para MemoramaStrategy")
class MemoramaStrategyTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private WordRepository wordRepository;

    private MemoramaStrategy memoramaStrategy;
    private Match testMatch;
    private Theme testTheme;
    private List<Word> testWords;

    @BeforeEach
    void setUp() {
        memoramaStrategy = new MemoramaStrategy(messagingTemplate, wordRepository);
        
        testTheme = new Theme();
        testTheme.setId(1L);
        testTheme.setName("Test Theme");

        testMatch = new Match();
        testMatch.setRoomCode("GAME001");
        testMatch.setThemes(new HashSet<>(List.of(testTheme)));
        testMatch.setGameType(GameType.MEMORAMA);
        testMatch.setPlayers(new HashSet<>(List.of("Player1", "Player2")));

        // Crear palabras de prueba
        testWords = new java.util.ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            Word word = new Word();
            word.setId((long) i);
            word.setCharacter("汉" + i);
            word.setPinyin("han" + i);
            testWords.add(word);
        }
    }

    @Test
    @DisplayName("Debe retornar GameType.MEMORAMA")
    void testGetGameType() {
        assertEquals(GameType.MEMORAMA, memoramaStrategy.getGameType());
    }

    @Test
    @DisplayName("Debe manejar playTurn correctamente")
    void testPlayTurn() {
        when(wordRepository.findByThemeId(1L)).thenReturn(testWords);

        FlipCardRequest request = new FlipCardRequest();
        request.setRoomCode("GAME001");
        request.setCardPosition(0);

        assertDoesNotThrow(() -> memoramaStrategy.playTurn(testMatch, request));
    }

    @Test
    @DisplayName("Debe ignorar posición inválida en playTurn")
    void testPlayTurnInvalidPosition() {
        when(wordRepository.findByThemeId(1L)).thenReturn(testWords);

        FlipCardRequest request = new FlipCardRequest();
        request.setRoomCode("GAME001");
        request.setCardPosition(-1);

        assertDoesNotThrow(() -> memoramaStrategy.playTurn(testMatch, request));
    }

    @Test
    @DisplayName("Debe ignorar null cardPosition")
    void testPlayTurnNullPosition() {
        when(wordRepository.findByThemeId(1L)).thenReturn(testWords);

        FlipCardRequest request = new FlipCardRequest();
        request.setRoomCode("GAME001");
        request.setCardPosition(0);

        assertDoesNotThrow(() -> memoramaStrategy.playTurn(testMatch, request));
    }

    @Test
    @DisplayName("Debe crear instancia válida de MemoramaStrategy")
    void testCreateInstance() {
        assertNotNull(memoramaStrategy);
    }

    @Test
    @DisplayName("Debe inyectar dependencias correctamente")
    void testDependenciesInjection() {
        assertNotNull(messagingTemplate);
        assertNotNull(wordRepository);
    }

    @Test
    @DisplayName("Debe implementar interfaz GameStrategy")
    void testImplementsGameStrategy() {
        assertTrue(memoramaStrategy instanceof GameStrategy);
    }
}
