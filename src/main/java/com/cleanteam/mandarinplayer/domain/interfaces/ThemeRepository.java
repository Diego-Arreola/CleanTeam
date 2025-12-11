package com.cleanteam.mandarinplayer.domain.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cleanteam.mandarinplayer.domain.entities.Theme;

import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    // Método opcional: Si alguna vez necesitas buscar por nombre para evitar duplicados
    Optional<Theme> findByName(String name);
}