package com.rmpi.telegram.wordchain.core.game;

import com.rmpi.telegram.wordchain.core.provider.WordProvider;

import java.util.List;
import java.util.Map;

public class VulnerableGame extends Game {
    public VulnerableGame(int targetId, WordProvider provider, Bot replyBot) {
        super(targetId, provider, replyBot);
    }

    public List<String> getRawWords() {
        return rawWords;
    }

    public Map<Character, List<String>> getCurrentWordMap() {
        return currentWordMap;
    }
}
