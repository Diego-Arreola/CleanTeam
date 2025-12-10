package com.cleanteam.mandarinplayer.domain.usecases;

import com.cleanteam.mandarinplayer.domain.entities.Theme;
import com.cleanteam.mandarinplayer.domain.entities.Word;
import com.cleanteam.mandarinplayer.domain.interfaces.ThemeRepository;
import com.cleanteam.mandarinplayer.infrastructure.dto.ThemeDTO;
import com.cleanteam.mandarinplayer.infrastructure.dto.WordDTO;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThemeUseCase {

    private final ThemeRepository themeRepository;

    public ThemeUseCase(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeDTO> getAllThemes() {
        return themeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ThemeDTO createTheme(ThemeDTO themeDTO) {
        Theme theme = new Theme();
        theme.setName(themeDTO.getName());
        theme.setDescription(themeDTO.getDescription());
        Theme savedTheme = themeRepository.save(theme);
        return mapToDTO(savedTheme);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public ThemeDTO getThemeById(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado"));
        return mapToDTO(theme);
    }

    public void deleteTheme(Long id) {
        themeRepository.deleteById(id);
    }

    private ThemeDTO mapToDTO(Theme theme) {
        ThemeDTO dto = new ThemeDTO();
        dto.setId(theme.getId());
        dto.setName(theme.getName());
        dto.setDescription(theme.getDescription());
        if (theme.getWords() != null) {
            dto.setVocabulary(theme.getWords().stream()
                    .map(word -> {
                        com.cleanteam.mandarinplayer.infrastructure.dto.WordDTO wordDTO = new com.cleanteam.mandarinplayer.infrastructure.dto.WordDTO();
                        wordDTO.setId(word.getId());
                        wordDTO.setCharacter(word.getCharacter());
                        wordDTO.setPinyin(word.getPinyin());
                        wordDTO.setTranslation(word.getTranslation());
                        wordDTO.setThemeId(theme.getId());
                        return wordDTO;
                    })
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}