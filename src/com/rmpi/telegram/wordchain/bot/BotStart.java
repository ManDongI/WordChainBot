package com.rmpi.telegram.wordchain.bot;

import com.rmpi.telegram.wordchain.core.provider.NewLineDelimiterTextWordProvider;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class BotStart {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Requires bot token for first argument");
            return;
        } else if (args.length < 2) {
            System.out.println("Requires bot username for second argument");
            return;
        } else if (args.length < 3) {
            System.out.println("Requires word list file path for third argument");
            return;
        }

        new NewLineDelimiterTextWordProvider(args[2]);
        TelegramBotsApi api = new TelegramBotsApi();

        try {
            api.registerBot(new WordChainBot(args[0], args[1]));
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}