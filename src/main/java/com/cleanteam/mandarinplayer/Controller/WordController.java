package com.cleanteam.mandarinplayer.Controller;

import com.cleanteam.mandarinplayer.DTO.WordDTO;
import com.cleanteam.mandarinplayer.Service.WordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/words")
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {
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