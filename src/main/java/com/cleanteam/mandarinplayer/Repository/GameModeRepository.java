package com.cleanteam.mandarinplayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cleanteam.mandarinplayer.model.GameMode;

public interface GameModeRepository extends JpaRepository<GameMode, Long> {
}
