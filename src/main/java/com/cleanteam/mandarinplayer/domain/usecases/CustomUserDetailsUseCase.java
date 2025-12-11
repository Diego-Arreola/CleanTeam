package com.cleanteam.mandarinplayer.domain.usecases;

import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cleanteam.mandarinplayer.domain.entities.AuthUser;
import com.cleanteam.mandarinplayer.domain.interfaces.AuthUserRepository;

@Service
public class CustomUserDetailsUseCase implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsUseCase.class);
    private final AuthUserRepository userRepository;

    public CustomUserDetailsUseCase(AuthUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        logger.info("--- INTENTO DE LOGIN ---");
        logger.info("Buscando usuario o correo: {}", input);

        Optional<AuthUser> userOpt = userRepository.findByUsername(input);
        
        if (userOpt.isPresent()) {
            logger.info(">> Encontrado por USERNAME: {}", userOpt.get().getUsername());
        } else {
            logger.info(">> No encontrado por username. Buscando por EMAIL...");
            userOpt = userRepository.findByEmail(input);
        }

        if (userOpt.isPresent()) {
             logger.info(">> USUARIO ENCONTRADO: {}", userOpt.get().getEmail());
        } else {
             logger.info(">> ERROR: Usuario NO existe en la BD.");
        }

        AuthUser u = userOpt.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con: " + input));

        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getUsername()) 
                .password(u.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}