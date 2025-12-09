package com.cleanteam.mandarinplayer.controller; 

import com.cleanteam.mandarinplayer.dto.CreateMatchRequest;
import com.cleanteam.mandarinplayer.dto.StartMatchRequest;
import com.cleanteam.mandarinplayer.model.Match;
import com.cleanteam.mandarinplayer.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
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
    public ResponseEntity<Match> playTurn(@PathVariable Long matchId, @RequestBody com.cleanteam.mandarinplayer.dto.FlipCardRequest request) {
        return ResponseEntity.ok(matchService.playTurn(matchId, request));
    }
}
