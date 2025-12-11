package com.cleanteam.mandarinplayer.infrastructure.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas para MatchResponse DTO")
class MatchResponseTest {

    private MatchResponse matchResponse;

    @BeforeEach
    void setUp() {
        matchResponse = new MatchResponse();
    }

    @Test
    @DisplayName("Debe establecer y obtener id")
    void testSetAndGetId() {
        matchResponse.setId(1L);
        assertEquals(1L, matchResponse.getId());
    }

    @Test
    @DisplayName("Debe establecer y obtener roomCode")
    void testSetAndGetRoomCode() {
        matchResponse.setRoomCode("ROOM456");
        assertEquals("ROOM456", matchResponse.getRoomCode());
    }

    @Test
    @DisplayName("Debe establecer y obtener status")
    void testSetAndGetStatus() {
        matchResponse.setStatus("ACTIVE");
        assertEquals("ACTIVE", matchResponse.getStatus());
    }

    @Test
    @DisplayName("Debe establecer y obtener currentPlayers")
    void testSetAndGetCurrentPlayers() {
        matchResponse.setCurrentPlayers(3);
        assertEquals(3, matchResponse.getCurrentPlayers());
    }

    @Test
    @DisplayName("Debe establecer y obtener maxPlayers")
    void testSetAndGetMaxPlayers() {
        matchResponse.setMaxPlayers(4);
        assertEquals(4, matchResponse.getMaxPlayers());
    }

    @Test
    @DisplayName("Debe establecer y obtener gameMode")
    void testSetAndGetGameMode() {
        matchResponse.setGameMode("MEMORAMA");
        assertEquals("MEMORAMA", matchResponse.getGameMode());
    }

    @Test
    @DisplayName("Debe establecer y obtener theme")
    void testSetAndGetTheme() {
        matchResponse.setTheme("Animals");
        assertEquals("Animals", matchResponse.getTheme());
    }

    @Test
    @DisplayName("Debe permitir null valores")
    void testNullValues() {
        matchResponse.setRoomCode(null);
        matchResponse.setStatus(null);
        matchResponse.setGameMode(null);
        matchResponse.setTheme(null);
        assertNull(matchResponse.getRoomCode());
        assertNull(matchResponse.getStatus());
        assertNull(matchResponse.getGameMode());
        assertNull(matchResponse.getTheme());
    }
}
