package com.cleanteam.mandarinplayer.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    void setUp() {
        wordController = new WordController(wordUseCase);
    }

    @Test
    @DisplayName("Debe crear instancia del controlador")
    void testCreateController() {
        assertNotNull(wordController);
    }

    @Test
    @DisplayName("Debe crear una palabra")
    void testCreateWord() {
        WordDTO wordDTO = new WordDTO();
        wordDTO.setCharacter("大");
        wordDTO.setPinyin("dà");
        wordDTO.setTranslation("big");
        
        when(wordUseCase.createWord(wordDTO)).thenReturn(wordDTO);
        
        ResponseEntity<WordDTO> response = wordController.createWord(wordDTO);
        
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("大", response.getBody().getCharacter());
    }

    @Test
    @DisplayName("Debe eliminar una palabra")
    void testDeleteWord() {
        ResponseEntity<Void> response = wordController.deleteWord(1L);
        
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
