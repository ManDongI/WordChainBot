package com.rmpi.telegram.wordchain.core.game;

import com.rmpi.telegram.wordchain.core.provider.WordProvider;

import java.util.*;

public class Game {
    private int targetId;
    private char next;
    private Bot replyBot;
    protected List<String> rawWords = new ArrayList<>();
    protected Map<Character, List<String>> currentWordMap = new HashMap<>();

    public Game(int targetId, WordProvider provider, Bot replyBot) {
        this.targetId = targetId;
        provider.initializeGame(this);
        this.next = provider.generateProperStartChar(this);
        this.replyBot = replyBot;
    }

    public int getTargetId() {
        return targetId;
    }

    public char getNext() {
        return next;
    }

    public void setCurrentWordMap(List<String> rawWords, Map<Character, List<String>> currentWordMap) {
        this.rawWords.clear();
        this.rawWords.addAll(rawWords);
        this.currentWordMap.clear();

        for (Character key : currentWordMap.keySet()) {
            List<String> wordList = new ArrayList<>();
            wordList.addAll(currentWordMap.get(key));
            this.currentWordMap.put(key, wordList);
        }
    }

    public boolean isInvalidWord(String word) {
        return word.length() < 2
                || !rawWords.contains(word)
                || !currentWordMap.getOrDefault(word.charAt(0), List.of()).contains(word);
    }

    public boolean acceptWord(String word) {
        if (!word.startsWith(String.valueOf(getNext())) || isInvalidWord(word))
            return false;
        else {
            rawWords.remove(word);
            currentWordMap.get(word.charAt(0)).remove(word);
            next = word.charAt(word.length() - 1);
            return true;
        }
    }

    public String doBotsTurn() {
        return replyBot.doTurn(this);
    }
}
