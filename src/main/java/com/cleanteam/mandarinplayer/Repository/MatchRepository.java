package com.cleanteam.mandarinplayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cleanteam.mandarinplayer.model.Match;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<Match> findByRoomCode(String roomCode);
    boolean existsByRoomCode(String roomCode);
}