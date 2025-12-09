package com.cleanteam.mandarinplayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cleanteam.mandarinplayer.model.Word;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    // Solo con escribir "findByThemeId", Spring sabe que debe hacer:
    // SELECT * FROM words WHERE theme_id = ?
    List<Word> findByThemeId(Long themeId);
}
