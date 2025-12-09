package com.cleanteam.mandarinplayer.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cleanteam.mandarinplayer.dto.WordDTO;
import com.cleanteam.mandarinplayer.model.Theme;
import com.cleanteam.mandarinplayer.model.Word;
import com.cleanteam.mandarinplayer.repository.ThemeRepository;
import com.cleanteam.mandarinplayer.repository.WordRepository;
import com.cleanteam.mandarinplayer.service.WordService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para WordService")
class WordServiceTest {

    @Mock
    private WordRepository wordRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private WordService wordService;

    private Word testWord;
    private WordDTO testWordDTO;
    private Theme testTheme;

    @BeforeEach
    void setUp() {
        testTheme = new Theme();
        testTheme.setId(1L);
        testTheme.setName("Basic Greetings");

        testWord = new Word();
        testWord.setId(1L);
        testWord.setCharacter("你好");
        testWord.setPinyin("ni hao");
        testWord.setTranslation("Hello");
        testWord.setTheme(testTheme);

        testWordDTO = new WordDTO();
        testWordDTO.setId(1L);
        testWordDTO.setCharacter("你好");
        testWordDTO.setPinyin("ni hao");
        testWordDTO.setTranslation("Hello");
        testWordDTO.setThemeId(1L);
    }

    @Test
    @DisplayName("Debe obtener palabras por tema")
    void testGetWordsByTheme() {
        List<Word> words = new ArrayList<>();
        words.add(testWord);

        Word word2 = new Word();
        word2.setId(2L);
        word2.setCharacter("谢谢");
        word2.setPinyin("xie xie");
        word2.setTranslation("Thank you");
        word2.setTheme(testTheme);
        words.add(word2);

        when(wordRepository.findByThemeId(1L)).thenReturn(words);

        List<WordDTO> result = wordService.getWordsByTheme(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("你好", result.get(0).getCharacter());
        assertEquals("谢谢", result.get(1).getCharacter());
        verify(wordRepository, times(1)).findByThemeId(1L);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando tema no tiene palabras")
    void testGetWordsByThemeEmpty() {
        when(wordRepository.findByThemeId(999L)).thenReturn(new ArrayList<>());

        List<WordDTO> result = wordService.getWordsByTheme(999L);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(wordRepository, times(1)).findByThemeId(999L);
    }

    @Test
    @DisplayName("Debe crear palabra correctamente")
    void testCreateWord() {
        when(themeRepository.findById(1L)).thenReturn(Optional.of(testTheme));
        when(wordRepository.save(any(Word.class))).thenReturn(testWord);

        WordDTO result = wordService.createWord(testWordDTO);

        assertNotNull(result);
        assertEquals("你好", result.getCharacter());
        assertEquals("ni hao", result.getPinyin());
        assertEquals("Hello", result.getTranslation());
        assertEquals(1L, result.getThemeId());
        verify(themeRepository, times(1)).findById(1L);
        verify(wordRepository, times(1)).save(any(Word.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando tema no existe")
    void testCreateWordThemeNotFound() {
        when(themeRepository.findById(999L)).thenReturn(Optional.empty());
        testWordDTO.setThemeId(999L);

        assertThrows(RuntimeException.class, () -> wordService.createWord(testWordDTO));
        verify(themeRepository, times(1)).findById(999L);
        verify(wordRepository, never()).save(any(Word.class));
    }

    @Test
    @DisplayName("Debe mapear Word a WordDTO correctamente")
    void testWordToDTOMapping() {
        when(wordRepository.findByThemeId(1L)).thenReturn(List.of(testWord));

        List<WordDTO> result = wordService.getWordsByTheme(1L);

        assertEquals(1, result.size());
        WordDTO dto = result.get(0);
        assertEquals(testWord.getId(), dto.getId());
        assertEquals(testWord.getCharacter(), dto.getCharacter());
        assertEquals(testWord.getPinyin(), dto.getPinyin());
        assertEquals(testWord.getTranslation(), dto.getTranslation());
        assertEquals(testWord.getTheme().getId(), dto.getThemeId());
    }

    @Test
    @DisplayName("Debe eliminar palabra correctamente")
    void testDeleteWord() {
        wordService.deleteWord(1L);

        verify(wordRepository, times(1)).deleteById(1L);
    }
}
