package com.cleanteam.mandarinplayer.Repository;

import com.cleanteam.mandarinplayer.Model.GameMode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameModeRepository extends JpaRepository<GameMode, Long> {
}
