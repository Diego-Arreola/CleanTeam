package com.cleanteam.mandarinplayer.Repository;

import com.cleanteam.mandarinplayer.Model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<Match> findByRoomCode(String roomCode);
    boolean existsByRoomCode(String roomCode);
}