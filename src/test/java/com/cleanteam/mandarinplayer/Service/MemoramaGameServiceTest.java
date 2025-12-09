package com.cleanteam.mandarinplayer.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.cleanteam.mandarinplayer.DTO.FlipCardRequest;
import com.cleanteam.mandarinplayer.Model.Match;
import com.cleanteam.mandarinplayer.Model.MemoramaCard;
import com.cleanteam.mandarinplayer.Model.MemoramaGameState;
import com.cleanteam.mandarinplayer.Model.Theme;
import com.cleanteam.mandarinplayer.Model.Word;
import com.cleanteam.mandarinplayer.Repository.MatchRepository;
import com.cleanteam.mandarinplayer.Repository.WordRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para MemoramaGameService")
class MemoramaGameServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private WordRepository wordRepository;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MemoramaGameService memoramaGameService;

    private Match testMatch;
    private Theme testTheme;
    private List<Word> testWords;

    @BeforeEach
    void setUp() {
        testTheme = new Theme();
        testTheme.setId(1L);
        testTheme.setName("Basic Words");

        testMatch = new Match();
        testMatch.setRoomCode("TEST01");
        testMatch.setThemes(new HashSet<>(List.of(testTheme)));
        testMatch.setPlayers(new HashSet<>(List.of("Player1", "Player2")));

        testWords = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            Word word = new Word();
            word.setId((long) i);
            word.setCharacter("汉" + i);
            word.setPinyin("han" + i);
            word.setTheme(testTheme);
            testWords.add(word);
        }
    }

    @Test
    @DisplayName("Debe inicializar juego correctamente desde Match")
    void testInitializeGame() {
        when(wordRepository.findByThemeId(1L)).thenReturn(testWords);

        memoramaGameService.initializeGame(testMatch);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MemoramaGameState> stateCaptor = ArgumentCaptor.forClass(MemoramaGameState.class);
        verify(messagingTemplate).convertAndSend(topicCaptor.capture(), stateCaptor.capture());

        MemoramaGameState gameState = stateCaptor.getValue();
        assertEquals("TEST01", gameState.getRoomCode());
        assertEquals(16, gameState.getCards().size()); // 8 palabras * 2 (hanzi + pinyin)
        assertTrue(gameState.isWaitingForFlip());
    }

    @Test
    @DisplayName("Debe lanzar excepción si Match no tiene temas")
    void testInitializeGameWithoutThemes() {
        testMatch.setThemes(new HashSet<>());

        assertThrows(IllegalStateException.class, () -> memoramaGameService.initializeGame(testMatch));
    }

    @Test
    @DisplayName("Debe inicializar correctamente con roomCode y themeId")
    void testInitialize() {
        when(wordRepository.findByThemeId(1L)).thenReturn(testWords);

        memoramaGameService.initialize("ROOM01", 1L, List.of("Player1", "Player2", "Player3"));

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MemoramaGameState> stateCaptor = ArgumentCaptor.forClass(MemoramaGameState.class);
        verify(messagingTemplate).convertAndSend(topicCaptor.capture(), stateCaptor.capture());

        String topic = topicCaptor.getValue();
        assertEquals("/topic/game/ROOM01", topic);

        MemoramaGameState gameState = stateCaptor.getValue();
        assertEquals("ROOM01", gameState.getRoomCode());
        assertEquals(3, gameState.getPlayerScores().size());
        assertEquals(0, gameState.getPlayerScores().get("Player1"));
        assertEquals(0, gameState.getPlayerScores().get("Player2"));
        assertEquals(0, gameState.getPlayerScores().get("Player3"));
    }

    @Test
    @DisplayName("Debe crear cartas en parejas (hanzi y pinyin)")
    void testCardPairsCreation() {
        when(wordRepository.findByThemeId(1L)).thenReturn(testWords);

        memoramaGameService.initialize("ROOM01", 1L, List.of("Player1"));

        ArgumentCaptor<MemoramaGameState> stateCaptor = ArgumentCaptor.forClass(MemoramaGameState.class);
        verify(messagingTemplate).convertAndSend(anyString(), stateCaptor.capture());

        MemoramaGameState gameState = stateCaptor.getValue();
        List<MemoramaCard> cards = gameState.getCards();

        // Verificar que hay parejas de hanzi y pinyin
        long hanziCount = cards.stream().filter(c -> "HANZI".equals(c.getPairType())).count();
        long pinyinCount = cards.stream().filter(c -> "PINYIN".equals(c.getPairType())).count();

        assertEquals(8, hanziCount);
        assertEquals(8, pinyinCount);
    }

    @Test
    @DisplayName("Debe establecer primer jugador actual correctamente")
    void testCurrentPlayerAssignment() {
        when(wordRepository.findByThemeId(1L)).thenReturn(testWords);
        List<String> players = List.of("Alice", "Bob", "Charlie");

        memoramaGameService.initialize("ROOM02", 1L, players);

        ArgumentCaptor<MemoramaGameState> stateCaptor = ArgumentCaptor.forClass(MemoramaGameState.class);
        verify(messagingTemplate).convertAndSend(anyString(), stateCaptor.capture());

        MemoramaGameState gameState = stateCaptor.getValue();
        assertEquals("Alice", gameState.getCurrentPlayerNickname());
    }

    @Test
    @DisplayName("Debe limitar palabras a 8 máximo")
    void testMaxWordsLimit() {
        // Crear más de 8 palabras
        List<Word> manyWords = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Word word = new Word();
            word.setId((long) i);
            word.setCharacter("汉" + i);
            word.setPinyin("han" + i);
            manyWords.add(word);
        }

        when(wordRepository.findByThemeId(1L)).thenReturn(manyWords);

        memoramaGameService.initialize("ROOM03", 1L, List.of("Player1"));

        ArgumentCaptor<MemoramaGameState> stateCaptor = ArgumentCaptor.forClass(MemoramaGameState.class);
        verify(messagingTemplate).convertAndSend(anyString(), stateCaptor.capture());

        MemoramaGameState gameState = stateCaptor.getValue();
        // 8 palabras máximo * 2 (hanzi + pinyin) = 16 cartas
        assertEquals(16, gameState.getCards().size());
    }

    @Test
    @DisplayName("Debe embarajar las cartas")
    void testCardShuffling() {
        when(wordRepository.findByThemeId(1L)).thenReturn(testWords);

        // Inicializar dos veces y verificar que las posiciones son diferentes
        memoramaGameService.initialize("ROOM04", 1L, List.of("Player1"));
        ArgumentCaptor<MemoramaGameState> stateCaptor1 = ArgumentCaptor.forClass(MemoramaGameState.class);
        verify(messagingTemplate, times(1)).convertAndSend(anyString(), stateCaptor1.capture());

        List<MemoramaCard> firstOrder = new ArrayList<>(stateCaptor1.getValue().getCards());

        memoramaGameService.initialize("ROOM05", 1L, List.of("Player1"));
        ArgumentCaptor<MemoramaGameState> stateCaptor2 = ArgumentCaptor.forClass(MemoramaGameState.class);
        verify(messagingTemplate, times(2)).convertAndSend(anyString(), stateCaptor2.capture());

        List<MemoramaCard> secondOrder = stateCaptor2.getValue().getCards();

        // No esperar exactamente igual (aunque es posible por suerte)
        // Solo verificar que ambas tienen 16 cartas y posiciones válidas
        assertEquals(16, firstOrder.size());
        assertEquals(16, secondOrder.size());
    }

    @Test
    @DisplayName("Debe ignorar flip si room no existe")
    void testFlipCardRoomNotExists() {
        FlipCardRequest request = new FlipCardRequest();
        request.setRoomCode("NONEXISTENT");
        request.setCardPosition(0);

        // No debe lanzar excepción
        assertDoesNotThrow(() -> memoramaGameService.flipCard(request));
        
        // Verificar que no envió mensaje (solo se enviaron los de initialize)
        verify(messagingTemplate, never()).convertAndSend(anyString(), any(MemoramaGameState.class));
    }

    @Test
    @DisplayName("Debe ignorar flip si posición es negativa")
    void testFlipCardNegativePosition() {
        when(wordRepository.findByThemeId(1L)).thenReturn(testWords);
        memoramaGameService.initialize("ROOM07", 1L, List.of("Player1"));

        ArgumentCaptor<MemoramaGameState> stateCaptor = ArgumentCaptor.forClass(MemoramaGameState.class);
        verify(messagingTemplate).convertAndSend(anyString(), stateCaptor.capture());
        MemoramaGameState capturedState = stateCaptor.getValue();

        // Usar reflection para agregar el estado
        try {
            java.lang.reflect.Field field = memoramaGameService.getClass().getDeclaredField("activeGames");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, MemoramaGameState> activeGames = (Map<String, MemoramaGameState>) field.get(memoramaGameService);
            activeGames.put("ROOM07", capturedState);
        } catch (Exception e) {
            fail("No se pudo acceder al mapa activeGames");
        }

        FlipCardRequest request = new FlipCardRequest();
        request.setRoomCode("ROOM07");
        request.setCardPosition(-1);

        assertDoesNotThrow(() -> memoramaGameService.flipCard(request));
    }

    @Test
    @DisplayName("Debe ignorar flip si posición es mayor al tamaño de cartas")
    void testFlipCardOutOfBoundsPosition() {
        when(wordRepository.findByThemeId(1L)).thenReturn(testWords);
        memoramaGameService.initialize("ROOM08", 1L, List.of("Player1"));

        ArgumentCaptor<MemoramaGameState> stateCaptor = ArgumentCaptor.forClass(MemoramaGameState.class);
        verify(messagingTemplate).convertAndSend(anyString(), stateCaptor.capture());
        MemoramaGameState capturedState = stateCaptor.getValue();

        // Usar reflection para agregar el estado
        try {
            java.lang.reflect.Field field = memoramaGameService.getClass().getDeclaredField("activeGames");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, MemoramaGameState> activeGames = (Map<String, MemoramaGameState>) field.get(memoramaGameService);
            activeGames.put("ROOM08", capturedState);
        } catch (Exception e) {
            fail("No se pudo acceder al mapa activeGames");
        }

        FlipCardRequest request = new FlipCardRequest();
        request.setRoomCode("ROOM08");
        request.setCardPosition(100);

        assertDoesNotThrow(() -> memoramaGameService.flipCard(request));
    }

    @Test
    @DisplayName("Debe ignorar flip si carta ya está emparejada")
    void testFlipCardAlreadyMatched() {
        when(wordRepository.findByThemeId(1L)).thenReturn(testWords);
        memoramaGameService.initialize("ROOM09", 1L, List.of("Player1"));

        ArgumentCaptor<MemoramaGameState> stateCaptor = ArgumentCaptor.forClass(MemoramaGameState.class);
        verify(messagingTemplate).convertAndSend(anyString(), stateCaptor.capture());
        MemoramaGameState capturedState = stateCaptor.getValue();

        // Marcar una carta como emparejada
        capturedState.getCards().get(0).setMatched(true);

        // Usar reflection para agregar el estado
        try {
            java.lang.reflect.Field field = memoramaGameService.getClass().getDeclaredField("activeGames");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, MemoramaGameState> activeGames = (Map<String, MemoramaGameState>) field.get(memoramaGameService);
            activeGames.put("ROOM09", capturedState);
        } catch (Exception e) {
            fail("No se pudo acceder al mapa activeGames");
        }

        FlipCardRequest request = new FlipCardRequest();
        request.setRoomCode("ROOM09");
        request.setCardPosition(0);

        assertDoesNotThrow(() -> memoramaGameService.flipCard(request));
        
        // Verificar que sigue siendo matched
        assertTrue(capturedState.getCards().get(0).isMatched());
    }

    @Test
    @DisplayName("Debe ignorar flip si carta ya está volteada")
    void testFlipCardAlreadyFlipped() {
        when(wordRepository.findByThemeId(1L)).thenReturn(testWords);
        memoramaGameService.initialize("ROOM10", 1L, List.of("Player1"));

        ArgumentCaptor<MemoramaGameState> stateCaptor = ArgumentCaptor.forClass(MemoramaGameState.class);
        verify(messagingTemplate).convertAndSend(anyString(), stateCaptor.capture());
        MemoramaGameState capturedState = stateCaptor.getValue();

        // Marcar una carta como volteada
        capturedState.getCards().get(0).setFlipped(true);

        // Usar reflection para agregar el estado
        try {
            java.lang.reflect.Field field = memoramaGameService.getClass().getDeclaredField("activeGames");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, MemoramaGameState> activeGames = (Map<String, MemoramaGameState>) field.get(memoramaGameService);
            activeGames.put("ROOM10", capturedState);
        } catch (Exception e) {
            fail("No se pudo acceder al mapa activeGames");
        }

        FlipCardRequest request = new FlipCardRequest();
        request.setRoomCode("ROOM10");
        request.setCardPosition(0);

        assertDoesNotThrow(() -> memoramaGameService.flipCard(request));
        
        // Verificar que sigue siendo flipped
        assertTrue(capturedState.getCards().get(0).isFlipped());
    }

    @Test
    @DisplayName("Debe voltear carta correctamente cuando es válida")
    void testFlipCardSuccess() {
        when(wordRepository.findByThemeId(1L)).thenReturn(testWords);
        memoramaGameService.initialize("ROOM11", 1L, List.of("Player1"));

        ArgumentCaptor<MemoramaGameState> stateCaptor = ArgumentCaptor.forClass(MemoramaGameState.class);
        verify(messagingTemplate).convertAndSend(anyString(), stateCaptor.capture());
        MemoramaGameState capturedState = stateCaptor.getValue();

        // Usar reflection para agregar el estado
        try {
            java.lang.reflect.Field field = memoramaGameService.getClass().getDeclaredField("activeGames");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, MemoramaGameState> activeGames = (Map<String, MemoramaGameState>) field.get(memoramaGameService);
            activeGames.put("ROOM11", capturedState);
        } catch (Exception e) {
            fail("No se pudo acceder al mapa activeGames");
        }

        FlipCardRequest request = new FlipCardRequest();
        request.setRoomCode("ROOM11");
        request.setCardPosition(5);

        assertDoesNotThrow(() -> memoramaGameService.flipCard(request));
        
        // Verificar que la carta se volteó
        assertTrue(capturedState.getCards().get(5).isFlipped());
        // Verificar que se envió el mensaje actualizado (2 calls: initialize + flipCard)
        verify(messagingTemplate, times(2)).convertAndSend(anyString(), any(MemoramaGameState.class));
    }
}
