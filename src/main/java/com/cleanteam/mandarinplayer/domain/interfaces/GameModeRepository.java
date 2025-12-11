package com.cleanteam.mandarinplayer.domain.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cleanteam.mandarinplayer.domain.entities.GameMode;

public interface GameModeRepository extends JpaRepository<GameMode, Long> {
}
