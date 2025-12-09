package com.cleanteam.mandarinplayer.Model;

public class PlayerSession {
    private final String sessionId;
    private final String nickname;
    private int score;

    public PlayerSession(String sessionId, String nickname, int score) {
        this.sessionId = sessionId;
        this.nickname = nickname;
        this.score = score;
    }

    public String getSessionId() { return sessionId; }
    public String getNickname() { return nickname; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
