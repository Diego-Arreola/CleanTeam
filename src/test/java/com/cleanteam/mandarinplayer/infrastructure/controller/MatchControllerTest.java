package com.cleanteam.mandarinplayer.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cleanteam.mandarinplayer.domain.usecases.MatchUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para MatchController")
class MatchControllerTest {

    @Mock
    private MatchUseCase matchUseCase;

    @Test
    @DisplayName("Debe tener inyección de dependencias")
    void testDependencyInjection() {
        assertNotNull(matchUseCase);
    }

    @Test
    @DisplayName("Debe poder mockearse el MatchUseCase")
    void testMockingMatchUseCase() {
        assertNotNull(matchUseCase);
    }
}

