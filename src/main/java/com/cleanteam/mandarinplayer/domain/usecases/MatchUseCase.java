package com.cleanteam.mandarinplayer.domain.usecases;

import com.cleanteam.mandarinplayer.domain.entities.GameType;
import com.cleanteam.mandarinplayer.domain.entities.Match;
import com.cleanteam.mandarinplayer.domain.entities.MatchStatus;
import com.cleanteam.mandarinplayer.domain.interfaces.GameModeRepository;
import com.cleanteam.mandarinplayer.domain.interfaces.MatchRepository;
import com.cleanteam.mandarinplayer.domain.interfaces.ThemeRepository;
import com.cleanteam.mandarinplayer.infrastructure.dto.CreateMatchRequest;
import com.cleanteam.mandarinplayer.infrastructure.dto.FlipCardRequest;
import com.cleanteam.mandarinplayer.infrastructure.dto.StartMatchRequest;
import com.cleanteam.mandarinplayer.infrastructure.gateways.MatchStateManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MatchUseCase {

    private final MatchRepository matchRepository;
    private final ThemeRepository themeRepository;
    private final GameModeRepository gameModeRepository;
    private final MatchConfigurer matchConfigurer;
    private final MatchStateManager matchStateManager;
    
    //STRATEGY: Un mapa inteligente de juegos
    private final Map<GameType, GameStrategy> strategies;

    public MatchUseCase(MatchRepository matchRepository,
                        ThemeRepository themeRepository,
                        GameModeRepository gameModeRepository,
                        MatchConfigurer matchConfigurer,
                        MatchStateManager matchStateManager,
                        List<GameStrategy> strategyList) { 
        
        this.matchRepository = matchRepository;
        this.themeRepository = themeRepository;
        this.gameModeRepository = gameModeRepository;
        this.matchConfigurer = matchConfigurer;
        this.matchStateManager = matchStateManager;

        // Convertimos la lista en un Mapa para buscar rápido
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(GameStrategy::getGameType, Function.identity()));
    }

    public Match createMatch(CreateMatchRequest request) {
         Match match = matchConfigurer.configure(request);
         return matchRepository.save(match);
    }

    @Transactional
    public Match startMatch(StartMatchRequest request) {
        // Lógica movida desde el StateManager viejo al Servicio
        Match match = matchRepository.findByRoomCode(request.getRoomCode())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (match.getPlayers().isEmpty()) {
            throw new IllegalStateException("Se necesita al menos un jugador");
        }

        match.setStatus(MatchStatus.IN_PROGRESS);
        Match savedMatch = matchRepository.save(match);
        
        // Avisar al lobby que el juego empezó (Usando el StateManager solo para notificar)
        matchStateManager.publishLobbyUpdate(savedMatch);

        return savedMatch;
    }

    public List<?> getAllThemes() { return themeRepository.findAll(); }
    public List<?> getAllGameModes() { return gameModeRepository.findAll(); }


    // --- NUEVO MÉTODO PARA JUGAR ---
    @Transactional
    public Match playTurn(Long matchId, FlipCardRequest request) {
        // 1. Buscamos la partida
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada"));

        // 2. Buscamos la estrategia correcta (Memorama, Word, etc.)
        GameStrategy strategy = strategies.get(match.getGameType());

        if (strategy == null) {
            throw new RuntimeException("No hay lógica implementada para el juego: " + match.getGameType());
        }

        // 3. Ejecutamos el turno (Polimorfismo puro)
        strategy.playTurn(match, request);

        // 4. Guardamos los cambios
        return matchRepository.save(match);
    }
}