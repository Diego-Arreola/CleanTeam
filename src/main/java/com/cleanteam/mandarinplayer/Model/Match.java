package com.cleanteam.mandarinplayer.Model;

import com.cleanteam.mandarinplayer.Game.GameType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 16, nullable = false)
    private String roomCode;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Enumerated(EnumType.STRING) // <--- USA ESTA ANOTACIÓN
    private GameType gameType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "match_themes",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    private Set<Theme> themes = new HashSet<>();

    // Simple para el lobby; si prefieres entidad Player, reemplazar por relación.
    @ElementCollection
    @CollectionTable(name = "match_players", joinColumns = @JoinColumn(name = "match_id"))
    @Column(name = "player_name")
    private Set<String> players = new HashSet<>();

    public Long getId() { return id; }

    public String getRoomCode() { return roomCode; }

    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public MatchStatus getStatus() { return status; }

    public void setStatus(MatchStatus status) { this.status = status; }

    public GameType getGameType() { return gameType; }

    public void setGameType(GameType gameType) { this.gameType = gameType; }

    public Set<Theme> getThemes() { return themes; }

    public void setThemes(Set<Theme> themes) { this.themes = themes; }

    public Set<String> getPlayers() { return players; }

    public void setPlayers(Set<String> players) { this.players = players; }
}