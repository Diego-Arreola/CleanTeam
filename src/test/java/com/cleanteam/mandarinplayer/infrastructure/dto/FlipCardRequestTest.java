package com.cleanteam.mandarinplayer.infrastructure.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas para FlipCardRequest DTO")
class FlipCardRequestTest {

    private FlipCardRequest flipCardRequest;

    @BeforeEach
    void setUp() {
        flipCardRequest = new FlipCardRequest();
    }

    @Test
    @DisplayName("Debe establecer y obtener roomCode")
    void testSetAndGetRoomCode() {
        flipCardRequest.setRoomCode("ROOM123");
        assertEquals("ROOM123", flipCardRequest.getRoomCode());
    }

    @Test
    @DisplayName("Debe establecer y obtener cardPosition")
    void testSetAndGetCardPosition() {
        flipCardRequest.setCardPosition(5);
        assertEquals(5, flipCardRequest.getCardPosition());
    }

    @Test
    @DisplayName("Debe establecer y obtener playerNickname")
    void testSetAndGetPlayerNickname() {
        flipCardRequest.setPlayerNickname("Player1");
        assertEquals("Player1", flipCardRequest.getPlayerNickname());
    }

    @Test
    @DisplayName("Debe crear instancia con valores por defecto")
    void testDefaultConstructor() {
        assertNotNull(flipCardRequest);
        assertNull(flipCardRequest.getRoomCode());
        assertNull(flipCardRequest.getPlayerNickname());
        assertEquals(0, flipCardRequest.getCardPosition());
    }

    @Test
    @DisplayName("Debe permitir null roomCode")
    void testNullRoomCode() {
        flipCardRequest.setRoomCode(null);
        assertNull(flipCardRequest.getRoomCode());
    }

    @Test
    @DisplayName("Debe permitir null playerNickname")
    void testNullPlayerNickname() {
        flipCardRequest.setPlayerNickname(null);
        assertNull(flipCardRequest.getPlayerNickname());
    }

    @Test
    @DisplayName("Debe soportar cardPosition negativa")
    void testNegativeCardPosition() {
        flipCardRequest.setCardPosition(-1);
        assertEquals(-1, flipCardRequest.getCardPosition());
    }

    @Test
    @DisplayName("Debe soportar cardPosition grande")
    void testLargeCardPosition() {
        flipCardRequest.setCardPosition(1000);
        assertEquals(1000, flipCardRequest.getCardPosition());
    }
}
