// java
package com.cleanteam.mandarinplayer.Game;

import com.cleanteam.mandarinplayer.Game.GameType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GameFactoryRegistry {

    private final Map<String, GameFactory> byTypeName = new HashMap<>();

    public GameFactoryRegistry(MemoramaGameFactory memoramaGameFactory) {
        byTypeName.put(GameType.MEMORAMA.name(), memoramaGameFactory);
    }

    public GameFactory getFactory(GameType type) {
        String key = type != null ? type.name() : GameType.MEMORAMA.name();
        return byTypeName.getOrDefault(key, byTypeName.get(GameType.MEMORAMA.name()));
    }
}