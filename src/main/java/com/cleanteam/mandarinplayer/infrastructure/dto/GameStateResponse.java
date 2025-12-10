// java
package com.cleanteam.mandarinplayer.infrastructure.dto;

import java.util.List;
import java.util.Map;

import com.cleanteam.mandarinplayer.domain.entities.MemoramaCard;

public class GameStateResponse {
    private String type;
    private String message;
    private List<MemoramaCard> cards;
    private Map<String, Integer> scores;
    private String currentPlayer;
    private String winner;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<MemoramaCard> getCards() { return cards; }
    public void setCards(List<MemoramaCard> cards) { this.cards = cards; }

    public Map<String, Integer> getScores() { return scores; }
    public void setScores(Map<String, Integer> scores) { this.scores = scores; }

    public String getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(String currentPlayer) { this.currentPlayer = currentPlayer; }

    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
}