package com.cleanteam.mandarinplayer.infrastructure.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas para StartMatchRequest DTO")
class StartMatchRequestTest {

    private StartMatchRequest startMatchRequest;

    @BeforeEach
    void setUp() {
        startMatchRequest = new StartMatchRequest();
    }

    @Test
    @DisplayName("Debe establecer y obtener roomCode")
    void testSetAndGetRoomCode() {
        startMatchRequest.setRoomCode("GAME123");
        assertEquals("GAME123", startMatchRequest.getRoomCode());
    }

    @Test
    @DisplayName("Debe permitir null roomCode")
    void testNullRoomCode() {
        startMatchRequest.setRoomCode(null);
        assertNull(startMatchRequest.getRoomCode());
    }

    @Test
    @DisplayName("Debe permitir roomCode vacío")
    void testEmptyRoomCode() {
        startMatchRequest.setRoomCode("");
        assertEquals("", startMatchRequest.getRoomCode());
    }
}
