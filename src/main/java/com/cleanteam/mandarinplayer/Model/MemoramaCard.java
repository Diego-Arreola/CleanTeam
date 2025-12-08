package com.cleanteam.mandarinplayer.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
