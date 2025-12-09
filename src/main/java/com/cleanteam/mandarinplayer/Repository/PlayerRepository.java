package com.cleanteam.mandarinplayer.Repository;

import com.cleanteam.mandarinplayer.Model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
