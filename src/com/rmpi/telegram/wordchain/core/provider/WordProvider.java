package com.rmpi.telegram.wordchain.core.provider;

import com.rmpi.telegram.wordchain.core.game.Game;

public interface WordProvider {
    char generateProperStartChar(Game game);
    void initializeGame(Game game);
}
