package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class TrackCommand implements Command {
    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "начать отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), """
            Реализация пока не добавлена!!+
            """);
    }
}
