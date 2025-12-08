package com.cleanteam.mandarinplayer.Service;

import com.cleanteam.mandarinplayer.Game.GameType;
import com.cleanteam.mandarinplayer.auth.repository.AuthUserRepository;
import com.cleanteam.mandarinplayer.DTO.CreateMatchRequest;
import com.cleanteam.mandarinplayer.Model.*;
import com.cleanteam.mandarinplayer.Repository.*;
import com.cleanteam.mandarinplayer.Repository.ThemeRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final ThemeRepository themeRepository;

    public MatchService(MatchRepository matchRepository,
                        ThemeRepository themeRepository
                       ) {
        this.matchRepository = matchRepository;
        this.themeRepository = themeRepository;
    }

    public Match createMatch(CreateMatchRequest request) {
        Match match = new Match();
        match.setRoomCode(generateRoomCode());
        match.setStatus(MatchStatus.CONFIGURING);
        match.setCreatedAt(LocalDateTime.now());

        // Asignar GameType enum desde el request
        if (request.getGameType() != null) {

            match.setGameType(GameType.valueOf(request.getGameType().toUpperCase()));

        }

        if (request.getThemeIds() != null) {
            match.setThemes(new HashSet<>(themeRepository.findAllById(request.getThemeIds())));
        }

        return matchRepository.save(match);
    }

    public Match startMatch(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        match.setStatus(MatchStatus.IN_PROGRESS);
        return matchRepository.save(match);
    }

    private String generateRoomCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

}

