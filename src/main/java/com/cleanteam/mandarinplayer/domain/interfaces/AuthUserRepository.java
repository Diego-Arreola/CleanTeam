package com.cleanteam.mandarinplayer.domain.interfaces;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cleanteam.mandarinplayer.domain.entities.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByUsername(String username);
    Optional<AuthUser> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
