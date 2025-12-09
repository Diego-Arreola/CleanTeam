package com.cleanteam.mandarinplayer.dto;

public class LobbyEvent {
    private String roomCode;
    private int playersCount;
    private String status;

    public LobbyEvent(String roomCode, int playersCount, String status) {
        this.roomCode = roomCode;
        this.playersCount = playersCount;
        this.status = status;
    }

    public String getRoomCode() { return roomCode; }
    public int getPlayersCount() { return playersCount; }
    public String getStatus() { return status; }
}