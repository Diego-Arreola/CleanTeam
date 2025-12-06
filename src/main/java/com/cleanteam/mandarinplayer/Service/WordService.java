package com.cleanteam.mandarinplayer.Service;

import com.cleanteam.mandarinplayer.DTO.WordDTO;
import com.cleanteam.mandarinplayer.Model.Theme;
import com.cleanteam.mandarinplayer.Model.Word;
import com.cleanteam.mandarinplayer.Repository.ThemeRepository;
import com.cleanteam.mandarinplayer.Repository.WordRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WordService {

    private final WordRepository wordRepository;
    private final ThemeRepository themeRepository;

    public WordService(WordRepository wordRepository, ThemeRepository themeRepository) {
        this.wordRepository = wordRepository;
        this.themeRepository = themeRepository;
    }

    public List<WordDTO> getWordsByTheme(Long themeId) {
        return wordRepository.findByThemeId(themeId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public WordDTO createWord(WordDTO wordDTO) {
        Theme theme = themeRepository.findById(wordDTO.getThemeId())
                .orElseThrow(() -> new RuntimeException("El tema con ID " + wordDTO.getThemeId() + " no existe"));

        Word word = new Word();
        word.setCharacter(wordDTO.getCharacter());
        word.setPinyin(wordDTO.getPinyin());
        word.setTranslation(wordDTO.getTranslation());

        theme.addWord(word);

        Word savedWord = wordRepository.save(word);

        return mapToDTO(savedWord);
    }

    public void deleteWord(Long id) {
        wordRepository.deleteById(id);
    }

    private WordDTO mapToDTO(Word word) {
        WordDTO dto = new WordDTO();
        dto.setId(word.getId());
        dto.setCharacter(word.getCharacter());
        dto.setPinyin(word.getPinyin());
        dto.setTranslation(word.getTranslation());
        dto.setThemeId(word.getTheme().getId());
        return dto;
    }
}