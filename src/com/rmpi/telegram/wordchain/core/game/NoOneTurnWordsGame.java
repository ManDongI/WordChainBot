package com.rmpi.telegram.wordchain.core.game;

import com.rmpi.telegram.wordchain.core.provider.WordProvider;

import java.util.List;

public class NoOneTurnWordsGame extends Game {
    public NoOneTurnWordsGame(int targetId, WordProvider provider, Bot replyBot) {
        super(targetId, provider, replyBot);
    }

    @Override
    public boolean isInvalidWord(String word) {
        return word.length() < 2
                || !rawWords.contains(word)
                || !currentWordMap.getOrDefault(word.charAt(0), List.of()).contains(word)
                || currentWordMap.getOrDefault(word.charAt(word.length() - 1), List.of()).isEmpty();
    }
}
