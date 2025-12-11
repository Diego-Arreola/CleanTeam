package com.cleanteam.mandarinplayer.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cleanteam.mandarinplayer.domain.usecases.WordUseCase;
import com.cleanteam.mandarinplayer.infrastructure.dto.WordDTO;

@RestController
@RequestMapping("/api/words")
public class WordController {

    private final WordUseCase wordService;

    public WordController(WordUseCase wordService) {
        this.wordService = wordService;
    }

    @PostMapping
    public ResponseEntity<WordDTO> createWord(@RequestBody WordDTO wordDTO) {
        return ResponseEntity.ok(wordService.createWord(wordDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        wordService.deleteWord(id);
        return ResponseEntity.noContent().build();
    }
}