package com.rmpi.telegram.wordchain.core.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomBot implements Bot {
    private Random rng = new Random();

    @Override
    public String doTurn(Game game) {
        List<String> possibles = new ArrayList<>(game.currentWordMap.get(game.getNext()));
        Collections.shuffle(possibles, rng);
        for (int i = 0; i < possibles.size(); i++)
            if (game.acceptWord(possibles.get(i)))
                return possibles.get(i);
        return "";
    }
}
