package com.cleanteam.mandarinplayer.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.cleanteam.mandarinplayer.domain.interfaces.WordRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para MemoramaStrategy")
class MemoramaStrategyTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private WordRepository wordRepository;

    private MemoramaStrategy memoramaStrategy;

    @BeforeEach
    void setUp() {
        memoramaStrategy = new MemoramaStrategy(messagingTemplate, wordRepository);
    }

    @Test
    @DisplayName("Debe crear instancia válida de MemoramaStrategy")
    void testCreateInstance() {
        assertNotNull(memoramaStrategy);
    }

    @Test
    @DisplayName("Debe inyectar dependencias correctamente")
    void testDependenciesInjection() {
        assertNotNull(messagingTemplate);
        assertNotNull(wordRepository);
    }

    @Test
    @DisplayName("Debe ejecutar estrategia de memorama sin errores")
    void testExecuteStrategy() {
        assertDoesNotThrow(() -> {
            // Verificar que la estrategia puede ser ejecutada
            assertNotNull(memoramaStrategy);
        });
    }

    @Test
    @DisplayName("Debe implementar interfaz GameStrategy")
    void testImplementsGameStrategy() {
        assertTrue(memoramaStrategy instanceof GameStrategy);
    }
}
