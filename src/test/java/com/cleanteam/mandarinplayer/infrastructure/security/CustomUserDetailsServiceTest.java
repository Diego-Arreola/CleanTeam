package com.cleanteam.mandarinplayer.infrastructure.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cleanteam.mandarinplayer.domain.entities.AuthUser;
import com.cleanteam.mandarinplayer.domain.interfaces.AuthUserRepository;
import com.cleanteam.mandarinplayer.domain.usecases.CustomUserDetailsUseCase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas para CustomUserDetailsService")
class CustomUserDetailsServiceTest {

    @Mock
    private AuthUserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsUseCase customUserDetailsService;

    private AuthUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new AuthUser();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword123");
    }

    @Test
    @DisplayName("Debe cargar usuario por username correctamente")
    void testLoadUserByUsernameWithUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword123", userDetails.getPassword());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Debe cargar usuario por email cuando no se encuentra por username")
    void testLoadUserByUsernameWithEmail() {
        when(userRepository.findByUsername("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword123", userDetails.getPassword());
        verify(userRepository, times(1)).findByUsername("test@example.com");
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Debe lanzar UsernameNotFoundException cuando usuario no existe")
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonexistent");
        });

        verify(userRepository, times(1)).findByUsername("nonexistent");
        verify(userRepository, times(1)).findByEmail("nonexistent");
    }

    @Test
    @DisplayName("Debe preferir encontrar por username antes que por email")
    void testLoadUserByUsernamePreferUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        // Verificar que solo se llamó a findByUsername, no a findByEmail
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    @DisplayName("Debe cargar usuario con authorities vacías")
    void testLoadUserByUsernameEmptyAuthorities() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails.getAuthorities());
        assertEquals(0, userDetails.getAuthorities().size());
    }

    @Test
    @DisplayName("Debe manejar input null correctamente")
    void testLoadUserByUsernameNull() {
        when(userRepository.findByUsername(null)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(null)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(null);
        });
    }

}
