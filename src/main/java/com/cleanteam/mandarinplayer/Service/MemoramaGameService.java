// java
package com.cleanteam.mandarinplayer.Service;

import com.cleanteam.mandarinplayer.DTO.FlipCardRequest;
import com.cleanteam.mandarinplayer.DTO.GameStateResponse;
import com.cleanteam.mandarinplayer.Model.Match;
import com.cleanteam.mandarinplayer.Model.MatchStatus;
import com.cleanteam.mandarinplayer.Model.MemoramaCard;
import com.cleanteam.mandarinplayer.Model.MemoramaGameState;
import com.cleanteam.mandarinplayer.Model.Word;
import com.cleanteam.mandarinplayer.Repository.MatchRepository;
import com.cleanteam.mandarinplayer.Repository.WordRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MemoramaGameService {

    private final Map<String, MemoramaGameState> activeGames = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final WordRepository wordRepository;
    private final MatchRepository matchRepository;

    public MemoramaGameService(SimpMessagingTemplate messagingTemplate,
                               WordRepository wordRepository,
                               MatchRepository matchRepository) {
        this.messagingTemplate = messagingTemplate;
        this.wordRepository = wordRepository;
        this.matchRepository = matchRepository;
    }

    // java
    public void initializeGame(Match match) {
        String roomCode = match.getRoomCode();
        Long themeId = match.getThemes().stream()
                .findFirst()
                .map(t -> t.getId())
                .orElseThrow(() -> new IllegalStateException("Match sin temas"));

        // Los jugadores ya son nicknames (String)
        List<String> playerNicknames = new ArrayList<>(match.getPlayers());

        initialize(roomCode, themeId, playerNicknames);
    }

    public void initialize(String roomCode, Long themeId, List<String> playerNicknames) {
        List<Word> words = wordRepository.findByThemeId(themeId);
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
        gameState.setCurrentPlayerNickname(playerNicknames.get(0));
        gameState.setWaitingForFlip(true);

        for (String nickname : playerNicknames) {
            gameState.getPlayerScores().put(nickname, 0);
        }

        messagingTemplate.convertAndSend("/topic/game/" + roomCode, gameState);


    }

    public void flipCard(FlipCardRequest request) {
        String roomCode = request.getRoomCode();
        Integer position = request.getCardPosition();

        MemoramaGameState state = activeGames.get(roomCode);
        if (state == null) {
            // Opcional: notificar error
            return;
        }

        if (position == null || position < 0 || position >= state.getCards().size()) {
            // Opcional: notificar error
            return;
        }

        MemoramaCard card = state.getCards().get(position);
        if (card.isMatched() || card.isFlipped()) {
            // Ignorar si ya está volteada o emparejada
            return;
        }

        card.setFlipped(true);

        // Lógica mínima: alternar espera y jugador actual si fuese necesario
        state.setWaitingForFlip(false);

        // Enviar el estado actualizado al tópico STOMP
        messagingTemplate.convertAndSend("/topic/game/" + roomCode, state);
    }

}