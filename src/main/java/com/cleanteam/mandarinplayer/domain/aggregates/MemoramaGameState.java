// java
package com.cleanteam.mandarinplayer.domain.aggregates;

import com.cleanteam.mandarinplayer.domain.entities.MemoramaCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoramaGameState {
    private String roomCode;
    private List<MemoramaCard> cards = new ArrayList<>();
    private String currentPlayerNickname;
    private boolean waitingForFlip;
    private boolean gameEnded;
    private String winner;
    private int firstCardIndex = -1;
    private int secondCardIndex = -1;
    private final Map<String, Integer> playerScores = new HashMap<>();

    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public List<MemoramaCard> getCards() { return cards; }
    public void setCards(List<MemoramaCard> cards) { this.cards = cards; }

    public String getCurrentPlayerNickname() { return currentPlayerNickname; }
    public void setCurrentPlayerNickname(String currentPlayerNickname) { this.currentPlayerNickname = currentPlayerNickname; }

    public boolean isWaitingForFlip() { return waitingForFlip; }
    public void setWaitingForFlip(boolean waitingForFlip) { this.waitingForFlip = waitingForFlip; }

    public boolean isGameEnded() { return gameEnded; }
    public void setGameEnded(boolean gameEnded) { this.gameEnded = gameEnded; }

    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }

    public int getFirstCardIndex() { return firstCardIndex; }
    public void setFirstCardIndex(int firstCardIndex) { this.firstCardIndex = firstCardIndex; }

    public int getSecondCardIndex() { return secondCardIndex; }
    public void setSecondCardIndex(int secondCardIndex) { this.secondCardIndex = secondCardIndex; }

    public Map<String, Integer> getPlayerScores() { return playerScores; }
}