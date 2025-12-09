package com.cleanteam.mandarinplayer.game;

import com.cleanteam.mandarinplayer.dto.FlipCardRequest;
import com.cleanteam.mandarinplayer.model.*;
import com.cleanteam.mandarinplayer.repository.WordRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MemoramaStrategy implements GameStrategy {

    // --- 1. ESTADO DEL JUEGO ---
    private final Map<String, MemoramaGameState> activeGames = new ConcurrentHashMap<>();
    
    // Dependencias necesarias
    private final SimpMessagingTemplate messagingTemplate;
    private final WordRepository wordRepository;

    public MemoramaStrategy(SimpMessagingTemplate messagingTemplate,
                            WordRepository wordRepository) {
        this.messagingTemplate = messagingTemplate;
        this.wordRepository = wordRepository;
    }

    @Override
    public GameType getGameType() {
        return GameType.MEMORAMA;
    }

    // --- 2. LÓGICA PRINCIPAL ---
    @Override
    public void playTurn(Match match, FlipCardRequest request) {
        String roomCode = match.getRoomCode();

        // A. AUTO-INICIALIZACIÓN: Si el juego no está en memoria, lo creamos ahora
        MemoramaGameState state = activeGames.get(roomCode);
        if (state == null) {
            state = initializeGameAndStore(match);
        }

        // B. LÓGICA DE FLIP
        Integer position = request.getCardPosition();

        // Validaciones
        if (position == null || position < 0 || position >= state.getCards().size()) {
            return; // O lanzar excepción
        }

        MemoramaCard card = state.getCards().get(position);
        if (card.isMatched() || card.isFlipped()) {
            return; // Carta ya volteada o emparejada, ignorar
        }

        // Ejecutar el movimiento
        card.setFlipped(true);
        state.setWaitingForFlip(!state.isWaitingForFlip());

        // C. NOTIFICAR AL FRONTEND (WebSocket)
        messagingTemplate.convertAndSend("/topic/game/" + roomCode, state);
        
        System.out.println("Turno jugado en Memorama. Sala: " + roomCode + ", Carta: " + position);
    }

    // --- 3. MÉTODOS PRIVADOS DE AYUDA ---

    private MemoramaGameState initializeGameAndStore(Match match) {
        String roomCode = match.getRoomCode();
        Long themeId = match.getThemes().stream()
                .findFirst()
                .map(Theme::getId) 
                .orElseThrow(() -> new IllegalStateException("Match sin temas"));

        List<String> playerNicknames = new ArrayList<>(match.getPlayers()); 
        

        MemoramaGameState gameState = buildInitialState(roomCode, themeId, playerNicknames);
        activeGames.put(roomCode, gameState);
        
        // Publicar estado inicial
        messagingTemplate.convertAndSend("/topic/game/" + roomCode, gameState);
        
        return gameState;
    }

    private MemoramaGameState buildInitialState(String roomCode, Long themeId, List<String> playerNicknames) {
        List<Word> words = wordRepository.findByThemeId(themeId);
        // Validación extra por seguridad
        if (words.isEmpty()) { throw new RuntimeException("No hay palabras para el tema ID: " + themeId); }
        
        List<Word> selectedWords = words.stream().limit(8).toList();

        List<MemoramaCard> cards = new ArrayList<>();
        int position = 0;
        for (Word word : selectedWords) {
            cards.add(new MemoramaCard(position++, word.getId(), word.getCharacter(), "HANZI", false, false));
            cards.add(new MemoramaCard(position++, word.getId(), word.getPinyin(), "PINYIN", false, false));
        }

        Collections.shuffle(cards);
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setPosition(i);
        }

        MemoramaGameState gameState = new MemoramaGameState();
        gameState.setRoomCode(roomCode);
        gameState.setCards(cards);
        // Validación por si la lista de jugadores está vacía
        if (!playerNicknames.isEmpty()) {
            gameState.setCurrentPlayerNickname(playerNicknames.get(0));
        }
        gameState.setWaitingForFlip(true);

        for (String nickname : playerNicknames) {
            gameState.getPlayerScores().put(nickname, 0);
        }
        return gameState;
    }
}