package com.cleanteam.mandarinplayer.domain.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cleanteam.mandarinplayer.domain.entities.Match;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<Match> findByRoomCode(String roomCode);
    boolean existsByRoomCode(String roomCode);
}