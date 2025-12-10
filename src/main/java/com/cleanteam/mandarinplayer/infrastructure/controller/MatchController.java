package com.cleanteam.mandarinplayer.infrastructure.controller; 

import com.cleanteam.mandarinplayer.domain.entities.Match;
import com.cleanteam.mandarinplayer.domain.usecases.MatchUseCase;
import com.cleanteam.mandarinplayer.infrastructure.dto.CreateMatchRequest;
import com.cleanteam.mandarinplayer.infrastructure.dto.StartMatchRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchUseCase matchService;

    public MatchController(MatchUseCase matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/themes")
    public ResponseEntity<?> getThemes() {
        return ResponseEntity.ok(matchService.getAllThemes());
    }

    @GetMapping("/gamemodes")
    public ResponseEntity<?> getGameModes() {
        return ResponseEntity.ok(matchService.getAllGameModes());
    }

    @PostMapping("/create")
    public ResponseEntity<Match> createMatch(@RequestBody CreateMatchRequest request) {
        return ResponseEntity.ok(matchService.createMatch(request));
    }

    @PostMapping("/matches/start")
    public ResponseEntity<Match> start(@RequestBody StartMatchRequest req) {
        return ResponseEntity.ok(matchService.startMatch(req));
    }
    @PostMapping("/{matchId}/play")
    public ResponseEntity<Match> playTurn(@PathVariable Long matchId, @RequestBody com.cleanteam.mandarinplayer.infrastructure.dto.FlipCardRequest request) {
        return ResponseEntity.ok(matchService.playTurn(matchId, request));
    }
}
