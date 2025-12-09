package com.cleanteam.mandarinplayer.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "game_modes")
public class GameMode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    // Getters y setters
}
