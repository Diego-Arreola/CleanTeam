package com.cleanteam.mandarinplayer.domain.aggregates;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas para MemoramaGameState agregado")
class MemoramaGameStateTest {

    private MemoramaGameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new MemoramaGameState();
    }

    @Test
    @DisplayName("Debe establecer y obtener roomCode")
    void testSetAndGetRoomCode() {
        gameState.setRoomCode("GAME001");
        assertEquals("GAME001", gameState.getRoomCode());
    }

    @Test
    @DisplayName("Debe establecer y obtener currentPlayerNickname")
    void testSetAndGetCurrentPlayerNickname() {
        gameState.setCurrentPlayerNickname("Player1");
        assertEquals("Player1", gameState.getCurrentPlayerNickname());
    }

    @Test
    @DisplayName("Debe establecer y obtener waitingForFlip")
    void testSetAndGetWaitingForFlip() {
        gameState.setWaitingForFlip(true);
        assertTrue(gameState.isWaitingForFlip());
        gameState.setWaitingForFlip(false);
        assertFalse(gameState.isWaitingForFlip());
    }

    @Test
    @DisplayName("Debe permitir null roomCode")
    void testNullRoomCode() {
        gameState.setRoomCode(null);
        assertNull(gameState.getRoomCode());
    }

    @Test
    @DisplayName("Debe permitir null playerNickname")
    void testNullPlayerNickname() {
        gameState.setCurrentPlayerNickname(null);
        assertNull(gameState.getCurrentPlayerNickname());
    }
}
