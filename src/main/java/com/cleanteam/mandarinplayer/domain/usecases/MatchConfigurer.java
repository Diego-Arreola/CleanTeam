// java
package com.cleanteam.mandarinplayer.domain.usecases;

import com.cleanteam.mandarinplayer.domain.entities.GameType;
import com.cleanteam.mandarinplayer.domain.entities.Match;
import com.cleanteam.mandarinplayer.domain.entities.MatchStatus;
import com.cleanteam.mandarinplayer.domain.interfaces.ThemeRepository;
import com.cleanteam.mandarinplayer.infrastructure.dto.CreateMatchRequest;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class MatchConfigurer {

    private final ThemeRepository themeRepository;

    public MatchConfigurer(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Match configure(CreateMatchRequest request) {
        Match match = new Match();
        match.setRoomCode(generateRoomCode());
        match.setCreatedAt(LocalDateTime.now());
        match.setStatus(MatchStatus.CONFIGURING);
        configureGameType(match, request.getGameType());
        configureThemes(match, request.getThemeIds());
        return match;
    }

    private void configureGameType(Match match, String gameType) {
        if (gameType == null || gameType.isBlank()) {
            throw new IllegalArgumentException("Game type is required");
        }
        GameType type = GameType.valueOf(gameType.toUpperCase()); // viene del enum
        match.setGameType(type);
    }

    private void configureThemes(Match match, List<Long> themeIds) {
        if (themeIds != null && !themeIds.isEmpty()) {
            match.setThemes(new HashSet<>(themeRepository.findAllById(themeIds)));
        }
    }

    private String generateRoomCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}