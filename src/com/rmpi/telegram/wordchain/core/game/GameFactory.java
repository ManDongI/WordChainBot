package com.rmpi.telegram.wordchain.core.game;

import com.rmpi.telegram.wordchain.core.provider.WordProvider;

public class GameFactory {
    public static Game generateGame(int userId, WordProvider provider, String gameSettings, String botSettings) {
        return new NoOneTurnWordsGame(userId, provider, new RandomBot()); // TBD
    }
}
