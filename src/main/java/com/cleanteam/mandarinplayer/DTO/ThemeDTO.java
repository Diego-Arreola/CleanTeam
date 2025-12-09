package com.cleanteam.mandarinplayer.DTO;

import lombok.Data;

@Data // Lombok genera getters, setters, toString, etc.
public class ThemeDTO {
    private Long id;
    private String name;
    private String description;
    private java.util.List<WordDTO> vocabulary;
}
