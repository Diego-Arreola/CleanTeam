package com.cleanteam.mandarinplayer.infrastructure.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas para JoinMatchRequest DTO")
class JoinMatchRequestTest {

    private JoinMatchRequest joinMatchRequest;

    @BeforeEach
    void setUp() {
        joinMatchRequest = new JoinMatchRequest();
    }

    @Test
    @DisplayName("Debe establecer y obtener roomCode")
    void testSetAndGetRoomCode() {
        joinMatchRequest.setRoomCode("ROOM001");
        assertEquals("ROOM001", joinMatchRequest.getRoomCode());
    }

    @Test
    @DisplayName("Debe establecer y obtener playerNickname")
    void testSetAndGetPlayerNickname() {
        joinMatchRequest.setNickname("Alice");
        assertEquals("Alice", joinMatchRequest.getNickname());
    }

    @Test
    @DisplayName("Debe permitir null valores")
    void testNullValues() {
        joinMatchRequest.setRoomCode(null);
        joinMatchRequest.setNickname(null);
        assertNull(joinMatchRequest.getRoomCode());
        assertNull(joinMatchRequest.getNickname());
    }
}
