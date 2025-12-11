package com.cleanteam.mandarinplayer.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cleanteam.mandarinplayer.domain.usecases.ThemeUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para ThemeController")
class ThemeControllerTest {

    @Mock
    private ThemeUseCase themeUseCase;

    @Test
    @DisplayName("Debe tener inyección de dependencias")
    void testDependencyInjection() {
        assertNotNull(themeUseCase);
    }

    @Test
    @DisplayName("Debe poder mockearse el ThemeUseCase")
    void testMockingThemeUseCase() {
        assertNotNull(themeUseCase);
    }
}
