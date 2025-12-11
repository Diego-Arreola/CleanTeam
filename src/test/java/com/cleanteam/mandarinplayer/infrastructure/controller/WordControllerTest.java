package com.cleanteam.mandarinplayer.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.cleanteam.mandarinplayer.domain.usecases.WordUseCase;
import com.cleanteam.mandarinplayer.infrastructure.dto.WordDTO;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para WordController")
class WordControllerTest {

    @Mock
    private WordUseCase wordUseCase;

    private WordController wordController;
    private WordDTO testWordDTO;

    @BeforeEach
    void setUp() {
        wordController = new WordController(wordUseCase);
        
        testWordDTO = new WordDTO();
        testWordDTO.setId(1L);
        testWordDTO.setCharacter("大");
        testWordDTO.setPinyin("dà");
        testWordDTO.setTranslation("big");
    }

    @Test
    @DisplayName("Debe crear instancia del controlador")
    void testCreateController() {
        assertNotNull(wordController);
    }

    @Test
    @DisplayName("Debe crear una palabra exitosamente")
    void testCreateWord() {
        when(wordUseCase.createWord(testWordDTO)).thenReturn(testWordDTO);
        
        ResponseEntity<WordDTO> response = wordController.createWord(testWordDTO);
        
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("大", response.getBody().getCharacter());
        assertEquals("dà", response.getBody().getPinyin());
        assertEquals("big", response.getBody().getTranslation());
    }

    @Test
    @DisplayName("Debe eliminar una palabra exitosamente")
    void testDeleteWord() {
        doNothing().when(wordUseCase).deleteWord(1L);
        
        ResponseEntity<Void> response = wordController.deleteWord(1L);
        
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(wordUseCase, times(1)).deleteWord(1L);
    }

    @Test
    @DisplayName("Debe retornar 204 No Content al eliminar")
    void testDeleteWordReturnsNoContent() {
        doNothing().when(wordUseCase).deleteWord(2L);
        
        ResponseEntity<Void> response = wordController.deleteWord(2L);
        
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Debe crear palabra con datos completos")
    void testCreateWordCompleteData() {
        WordDTO completeWord = new WordDTO();
        completeWord.setId(2L);
        completeWord.setCharacter("小");
        completeWord.setPinyin("xiǎo");
        completeWord.setTranslation("small");
        
        when(wordUseCase.createWord(completeWord)).thenReturn(completeWord);
        
        ResponseEntity<WordDTO> response = wordController.createWord(completeWord);
        
        assertNotNull(response.getBody());
        assertEquals("小", response.getBody().getCharacter());
    }

    @Test
    @DisplayName("Debe verificar que WordUseCase es llamado al crear")
    void testCreateWordCallsUseCase() {
        when(wordUseCase.createWord(testWordDTO)).thenReturn(testWordDTO);
        
        wordController.createWord(testWordDTO);
        
        verify(wordUseCase, times(1)).createWord(testWordDTO);
    }
}
