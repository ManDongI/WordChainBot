package com.rmpi.telegram.wordchain.core.provider;

import com.rmpi.telegram.wordchain.core.game.Game;

import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class NewLineDelimiterTextWordProvider implements WordProvider {
    private static NewLineDelimiterTextWordProvider instance;
    private Random rng = new Random();
    private List<String> words = new ArrayList<>();
    private Map<Character, List<String>> wordsMap = new HashMap<>();

    public NewLineDelimiterTextWordProvider(String wordPath) {
        if (instance != null)
            throw new AssertionError();

        try {
            words.addAll(Files.readAllLines(Paths.get(wordPath)));
            words.removeIf(String::isEmpty);
            wordsMap.putAll(words.stream()
                    .collect(
                            Collectors.groupingBy(
                                    str -> str.charAt(0),
                                    Collectors.toCollection(ArrayList::new)
                            )
                    ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        instance = this;
    }

    @NotNull
    public static NewLineDelimiterTextWordProvider getInstance() {
        return instance;
    }

    @Override
    public char generateProperStartChar(Game game) {
        List<Character> startList = wordsMap.keySet().stream()
                .filter(
                        start -> wordsMap.get(start).size() >= 300
                ).collect(Collectors.toList());
        return startList.get(rng.nextInt(startList.size()));
    }

    @Override
    public void initializeGame(Game game) {
        game.setCurrentWordMap(
                words,
                wordsMap
        );
    }
}
