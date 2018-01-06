package com.rmpi.telegram.wordchain.bot;

import com.rmpi.telegram.wordchain.core.game.Game;
import com.rmpi.telegram.wordchain.core.game.GameCenter;
import com.rmpi.telegram.wordchain.core.game.RandomBot;
import com.rmpi.telegram.wordchain.core.game.VulnerableGame;
import com.rmpi.telegram.wordchain.core.provider.NewLineDelimiterTextWordProvider;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordChainBot extends TelegramLongPollingBot {
    private final String TOKEN, USERNAME;
    private final GameCenter CENTER = GameCenter.getInstance();
    private VulnerableGame hits = new VulnerableGame(0, NewLineDelimiterTextWordProvider.getInstance(), new RandomBot());

    public WordChainBot(String token, String username) {
        TOKEN = token;
        USERNAME = username;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage())
            return;
        Message request = update.getMessage();
        if (!request.hasText() || !request.isCommand())
            return;
        String message = request.getText();
        assert message.startsWith("/");
        List<String> tokens = new ArrayList<>(Arrays.asList(message.split(" ")));
        String command = tokens.remove(0).substring(1);

        if (command.contains("@")) {
            String[] commandSplit = command.split("@");
            if (commandSplit.length > 2) return;
            if (!commandSplit[1].equals(getBotUsername())) return;
            command = commandSplit[0];
        }

        int targetId = update.getMessage().getFrom().getId();

        switch (command) {
            case "startgame": {
                new Thread(() -> {
                    SendMessage response = new SendMessage();
                    response.setChatId(String.valueOf(request.getChatId()));
                    response.setReplyToMessageId(request.getMessageId());
                    response.disableWebPagePreview();
                    if (tokens.size() != 2)
                        response.setText("잘못된 명령어입니다.");
                    else if (CENTER.newGame(targetId, NewLineDelimiterTextWordProvider.getInstance(), tokens.get(0), tokens.get(1)))
                        response.setText("끝말잇기 시작, 현재 무조건 한방단어는 금지하며, 봇의 수준은 '랜덤' 입니다. 시작 단어: " + CENTER.getGame(targetId).getNext());
                    else
                        response.setText("이미 진행중입니다.");

                    try {
                        sendMessage(response);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
                break;
            case "next": {
                new Thread(() -> {
                    SendMessage response = new SendMessage();
                    response.setChatId(String.valueOf(request.getChatId()));
                    response.setReplyToMessageId(request.getMessageId());
                    response.disableWebPagePreview();
                    if (tokens.size() != 1)
                        response.setText("잘못된 명령어입니다.");
                    else if (CENTER.hasGame(targetId)) {
                        Game currentGame = CENTER.getGame(targetId);
                        if (currentGame.acceptWord(tokens.get(0))) {
                            String botsReply = currentGame.doBotsTurn();
                            if (botsReply.length() >= 2)
                                response.setText("봇의 턴: " + botsReply);
                            else {
                                response.setText("당신의 승리!");
                                CENTER.terminateGame(targetId);
                            }
                        } else
                            response.setText("부적절한 단어입니다. 다음으로 시작하는 올바른 단어를 사용해주세요! " + currentGame.getNext());
                    } else
                        response.setText("끝말잇기를 진행중이지 않습니다.");

                    try {
                        sendMessage(response);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
                break;
            case "giveup": {
                new Thread(() -> {
                    SendMessage response = new SendMessage();
                    response.setChatId(String.valueOf(request.getChatId()));
                    response.setReplyToMessageId(request.getMessageId());
                    response.disableWebPagePreview();
                    if (tokens.size() != 0)
                        response.setText("잘못된 명령어입니다.");
                    else if (CENTER.hasGame(targetId)) {
                        CENTER.terminateGame(targetId);
                        response.setText("당신의 패배!");
                    } else
                        response.setText("끝말잇기를 진행중이지 않습니다.");

                    try {
                        sendMessage(response);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }
}