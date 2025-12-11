package com.cleanteam.mandarinplayer.domain.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas para PlayerSession entidad")
class PlayerSessionTest {

    private PlayerSession playerSession;

    @BeforeEach
    void setUp() {
        playerSession = new PlayerSession("SESSION123", "PlayerOne", 0);
    }

    @Test
    @DisplayName("Debe crear instancia con parámetros")
    void testConstructorWithParameters() {
        assertNotNull(playerSession);
        assertEquals("SESSION123", playerSession.getSessionId());
        assertEquals("PlayerOne", playerSession.getNickname());
        assertEquals(0, playerSession.getScore());
    }

    @Test
    @DisplayName("Debe obtener sessionId")
    void testGetSessionId() {
        assertEquals("SESSION123", playerSession.getSessionId());
    }

    @Test
    @DisplayName("Debe obtener nickname")
    void testGetNickname() {
        assertEquals("PlayerOne", playerSession.getNickname());
    }

    @Test
    @DisplayName("Debe establecer y obtener score")
    void testSetAndGetScore() {
        playerSession.setScore(10);
        assertEquals(10, playerSession.getScore());
    }

    @Test
    @DisplayName("Debe permitir score negativo")
    void testNegativeScore() {
        playerSession.setScore(-5);
        assertEquals(-5, playerSession.getScore());
    }

    @Test
    @DisplayName("Debe crear múltiples instancias independientes")
    void testMultipleInstances() {
        PlayerSession session1 = new PlayerSession("SESSION1", "Player1", 5);
        PlayerSession session2 = new PlayerSession("SESSION2", "Player2", 3);
        
        assertEquals("SESSION1", session1.getSessionId());
        assertEquals("SESSION2", session2.getSessionId());
        assertEquals("Player1", session1.getNickname());
        assertEquals("Player2", session2.getNickname());
    }
}
