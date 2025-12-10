package com.cleanteam.mandarinplayer.infrastructure.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cleanteam.mandarinplayer.infrastructure.security.JwtUtils;

import org.junit.jupiter.api.DisplayName;

@DisplayName("Pruebas para JwtUtils")
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
    }

    @Test
    @DisplayName("Debe generar un token válido para un usuario")
    void testGenerateToken() {
        String username = "testuser";
        String token = jwtUtils.generateToken(username);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT tiene 3 partes separadas por puntos
    }

    @Test
    @DisplayName("Debe generar tokens para múltiples usuarios correctamente")
    void testGenerateTokenIsUnique() {
        String username1 = "testuser1";
        String username2 = "testuser2";
        String token1 = jwtUtils.generateToken(username1);
        String token2 = jwtUtils.generateToken(username2);

        assertNotNull(token1);
        assertNotNull(token2);
        // Los tokens tienen diferentes usuarios en el payload
        assertEquals(username1, jwtUtils.extractUsername(token1));
        assertEquals(username2, jwtUtils.extractUsername(token2));
    }

    @Test
    @DisplayName("Debe validar un token válido generado")
    void testIsTokenValid() {
        String username = "testuser";
        String token = jwtUtils.generateToken(username);

        assertTrue(jwtUtils.isTokenValid(token));
    }

    @Test
    @DisplayName("Debe rechazar un token inválido")
    void testIsTokenInvalid() {
        String invalidToken = "invalid.token.here";

        assertFalse(jwtUtils.isTokenValid(invalidToken));
    }

    @Test
    @DisplayName("Debe rechazar un token vacío")
    void testIsTokenEmpty() {
        assertFalse(jwtUtils.isTokenValid(""));
    }

    @Test
    @DisplayName("Debe rechazar null como token")
    void testIsTokenNull() {
        assertFalse(jwtUtils.isTokenValid(null));
    }

    @Test
    @DisplayName("Debe extraer el username correctamente del token")
    void testExtractUsername() {
        String username = "testuser123";
        String token = jwtUtils.generateToken(username);

        String extractedUsername = jwtUtils.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    @DisplayName("Debe lanzar excepción al extraer username de un token inválido")
    void testExtractUsernameInvalidToken() {
        String invalidToken = "invalid.token.here";

        assertThrows(Exception.class, () -> jwtUtils.extractUsername(invalidToken));
    }

    @Test
    @DisplayName("Debe extraer username correctamente de diferentes usuarios")
    void testExtractUsernameDifferentUsers() {
        String user1 = "usuario1";
        String user2 = "usuario2";

        String token1 = jwtUtils.generateToken(user1);
        String token2 = jwtUtils.generateToken(user2);

        assertEquals(user1, jwtUtils.extractUsername(token1));
        assertEquals(user2, jwtUtils.extractUsername(token2));
    }

}
