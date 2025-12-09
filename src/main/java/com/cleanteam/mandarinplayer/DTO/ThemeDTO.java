package com.cleanteam.mandarinplayer.dto;

import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // Lombok genera getters, setters, toString, etc.
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDTO {
    private Long id;
    private String name;
    private String description;
    private java.util.List<WordDTO> vocabulary;
}
