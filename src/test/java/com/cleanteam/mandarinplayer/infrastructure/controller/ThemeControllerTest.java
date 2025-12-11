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

import com.cleanteam.mandarinplayer.domain.usecases.ThemeUseCase;
import com.cleanteam.mandarinplayer.domain.usecases.WordUseCase;
import com.cleanteam.mandarinplayer.infrastructure.dto.ThemeDTO;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para ThemeController")
class ThemeControllerTest {

    @Mock
    private ThemeUseCase themeUseCase;

    @Mock
    private WordUseCase wordUseCase;

    private ThemeController themeController;

    @BeforeEach
    void setUp() {
        // Crear instancia real del controlador con mocks
        themeController = new ThemeController(themeUseCase, wordUseCase);
    }

    @Test
    @DisplayName("Debe crear instancia del controlador")
    void testCreateController() {
        assertNotNull(themeController);
    }

    @Test
    @DisplayName("Debe obtener todos los temas")
    void testGetAllThemes() {
        ThemeDTO theme = new ThemeDTO();
        theme.setName("Colors");
        
        when(themeUseCase.getAllThemes()).thenReturn(List.of(theme));
        
        ResponseEntity<List<ThemeDTO>> response = themeController.getAllThemes();
        
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Debe obtener un tema por ID")
    void testGetThemeById() {
        ThemeDTO theme = new ThemeDTO();
        theme.setId(1L);
        theme.setName("Colors");
        
        when(themeUseCase.getThemeById(1L)).thenReturn(theme);
        
        ResponseEntity<ThemeDTO> response = themeController.getThemeById(1L);
        
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    @DisplayName("Debe crear un tema")
    void testCreateTheme() {
        ThemeDTO inputTheme = new ThemeDTO();
        inputTheme.setName("Animals");
        
        ThemeDTO createdTheme = new ThemeDTO();
        createdTheme.setId(1L);
        createdTheme.setName("Animals");
        
        when(themeUseCase.createTheme(inputTheme)).thenReturn(createdTheme);
        
        ResponseEntity<ThemeDTO> response = themeController.createTheme(inputTheme);
        
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("Animals", response.getBody().getName());
    }

    @Test
    @DisplayName("Debe eliminar un tema")
    void testDeleteTheme() {
        ResponseEntity<Void> response = themeController.deleteTheme(1L);
        
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}

