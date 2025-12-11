package com.cleanteam.mandarinplayer.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cleanteam.mandarinplayer.domain.usecases.WordUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para WordController")
class WordControllerTest {

    @Mock
    private WordUseCase wordUseCase;

    @Test
    @DisplayName("Debe tener inyección de dependencias")
    void testDependencyInjection() {
        assertNotNull(wordUseCase);
    }

    @Test
    @DisplayName("Debe poder mockearse el WordUseCase")
    void testMockingWordUseCase() {
        assertNotNull(wordUseCase);
    }
}
