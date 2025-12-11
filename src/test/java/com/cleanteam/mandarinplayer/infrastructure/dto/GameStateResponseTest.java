package com.cleanteam.mandarinplayer.infrastructure.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cleanteam.mandarinplayer.domain.entities.MemoramaCard;

@DisplayName("Pruebas para GameStateResponse DTO")
class GameStateResponseTest {

    private GameStateResponse gameStateResponse;

    @BeforeEach
    void setUp() {
        gameStateResponse = new GameStateResponse();
    }

    @Test
    @DisplayName("Debe crear instancia válida")
    void testCreateInstance() {
        assertNotNull(gameStateResponse);
    }

    @Test
    @DisplayName("Debe establecer y obtener type")
    void testSetAndGetType() {
        gameStateResponse.setType("GAME_STATE");
        assertEquals("GAME_STATE", gameStateResponse.getType());
    }

    @Test
    @DisplayName("Debe establecer y obtener message")
    void testSetAndGetMessage() {
        gameStateResponse.setMessage("Match started");
        assertEquals("Match started", gameStateResponse.getMessage());
    }

    @Test
    @DisplayName("Debe establecer y obtener currentPlayer")
    void testSetAndGetCurrentPlayer() {
        gameStateResponse.setCurrentPlayer("Player1");
        assertEquals("Player1", gameStateResponse.getCurrentPlayer());
    }

    @Test
    @DisplayName("Debe establecer y obtener winner")
    void testSetAndGetWinner() {
        gameStateResponse.setWinner("Player2");
        assertEquals("Player2", gameStateResponse.getWinner());
    }

    @Test
    @DisplayName("Debe establecer y obtener cards list")
    void testSetAndGetCards() {
        List<MemoramaCard> cards = new ArrayList<>();
        gameStateResponse.setCards(cards);
        assertEquals(cards, gameStateResponse.getCards());
    }

    @Test
    @DisplayName("Debe establecer y obtener scores map")
    void testSetAndGetScores() {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Player1", 5);
        gameStateResponse.setScores(scores);
        assertEquals(scores, gameStateResponse.getScores());
        assertEquals(5, gameStateResponse.getScores().get("Player1"));
    }

    @Test
    @DisplayName("Debe permitir null valores")
    void testNullValues() {
        gameStateResponse.setType(null);
        gameStateResponse.setMessage(null);
        gameStateResponse.setCurrentPlayer(null);
        gameStateResponse.setWinner(null);
        gameStateResponse.setCards(null);
        gameStateResponse.setScores(null);
        assertNull(gameStateResponse.getType());
        assertNull(gameStateResponse.getMessage());
        assertNull(gameStateResponse.getCurrentPlayer());
        assertNull(gameStateResponse.getWinner());
        assertNull(gameStateResponse.getCards());
        assertNull(gameStateResponse.getScores());
    }
}
