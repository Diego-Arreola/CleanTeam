package com.cleanteam.mandarinplayer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemoramaCard {
    private int position;
    private Long wordId;
    private String content;
    private String pairType;
    private boolean flipped;
    private boolean matched;
}
