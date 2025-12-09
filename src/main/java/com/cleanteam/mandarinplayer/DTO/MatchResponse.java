package com.cleanteam.mandarinplayer.dto;

public class MatchResponse {
    private Long id;
    private String roomCode;
    private String gameMode;
    private String theme;
    private String status;
    private Integer currentPlayers;
    private Integer maxPlayers;

    public MatchResponse() {}

    public MatchResponse(Long id, String roomCode, String gameMode, String theme,
                         String status, Integer currentPlayers, Integer maxPlayers) {
        this.id = id;
        this.roomCode = roomCode;
        this.gameMode = gameMode;
        this.theme = theme;
        this.status = status;
        this.currentPlayers = currentPlayers;
        this.maxPlayers = maxPlayers;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public String getGameMode() { return gameMode; }
    public void setGameMode(String gameMode) { this.gameMode = gameMode; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getCurrentPlayers() { return currentPlayers; }
    public void setCurrentPlayers(Integer currentPlayers) { this.currentPlayers = currentPlayers; }

    public Integer getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(Integer maxPlayers) { this.maxPlayers = maxPlayers; }
}