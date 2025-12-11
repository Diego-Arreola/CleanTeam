package com.cleanteam.mandarinplayer.domain.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas para Word entidad")
class WordTest {

    private Word word;

    @BeforeEach
    void setUp() {
        word = new Word();
    }

    @Test
    @DisplayName("Debe establecer y obtener ID")
    void testSetAndGetId() {
        word.setId(1L);
        assertEquals(1L, word.getId());
    }

    @Test
    @DisplayName("Debe establecer y obtener character")
    void testSetAndGetCharacter() {
        word.setCharacter("学");
        assertEquals("学", word.getCharacter());
    }

    @Test
    @DisplayName("Debe establecer y obtener pinyin")
    void testSetAndGetPinyin() {
        word.setPinyin("xué");
        assertEquals("xué", word.getPinyin());
    }

    @Test
    @DisplayName("Debe establecer y obtener translation")
    void testSetAndGetTranslation() {
        word.setTranslation("study");
        assertEquals("study", word.getTranslation());
    }

    @Test
    @DisplayName("Debe permitir null values")
    void testNullValues() {
        word.setCharacter(null);
        word.setPinyin(null);
        word.setTranslation(null);
        assertNull(word.getCharacter());
        assertNull(word.getPinyin());
        assertNull(word.getTranslation());
    }
}
