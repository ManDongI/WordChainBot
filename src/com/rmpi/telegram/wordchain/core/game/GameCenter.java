package com.rmpi.telegram.wordchain.core.game;

import com.rmpi.telegram.wordchain.core.provider.WordProvider;

import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameCenter {
    private static GameCenter instance = new GameCenter();

    private GameCenter() {
        if (instance != null)
            throw new AssertionError();
    }

    public static GameCenter getInstance() {
        return instance;
    }

    private List<Game> currentGames = new ArrayList<>();

    public boolean newGame(int userId, WordProvider provider, String gameSettings, String botSettings) {
        if (hasGame(userId))
            return false;
        Game generatedGame = GameFactory.generateGame(userId, provider, gameSettings, botSettings);
        currentGames.add(generatedGame);
        return true;
    }

    public boolean hasGame(int userId) {
        return currentGames.parallelStream()
                .anyMatch(game -> game.getTargetId() == userId);
    }

    @NotNull
    public Game getGame(int userId) {
        return currentGames.stream()
                .filter(game -> game.getTargetId() == userId)
                .collect(Collectors.toList())
                .get(0);
    }

    public void terminateGame(int userId) {
        currentGames.removeIf(game -> game.getTargetId() == userId);
    }
}
