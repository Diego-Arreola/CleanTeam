package com.cleanteam.mandarinplayer.model;
import com.cleanteam.mandarinplayer.auth.model.AuthUser;
import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private AuthUser user; // null si es no registrado

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    private int score;

    // Getters y setters
}
