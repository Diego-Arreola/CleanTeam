package com.cleanteam.mandarinplayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cleanteam.mandarinplayer.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
