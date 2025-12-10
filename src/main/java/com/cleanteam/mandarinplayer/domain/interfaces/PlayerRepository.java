package com.cleanteam.mandarinplayer.domain.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cleanteam.mandarinplayer.domain.entities.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
