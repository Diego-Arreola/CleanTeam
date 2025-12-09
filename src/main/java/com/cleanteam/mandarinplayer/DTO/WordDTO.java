package com.cleanteam.mandarinplayer.dto;

import lombok.Data;

@Data
public class WordDTO {
    private Long id;
    private String character;
    private String pinyin;
    private String translation;
    private Long themeId; // Importante: Solo recibimos el ID del tema, no el objeto entero
}
