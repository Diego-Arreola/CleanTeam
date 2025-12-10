// java
package com.cleanteam.mandarinplayer.infrastructure.dto;

public class FlipCardRequest {
    private String roomCode;
    private String playerNickname;
    private int cardPosition;

    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public String getPlayerNickname() { return playerNickname; }
    public void setPlayerNickname(String playerNickname) { this.playerNickname = playerNickname; }

    public int getCardPosition() { return cardPosition; }
    public void setCardPosition(int cardPosition) { this.cardPosition = cardPosition; }
}