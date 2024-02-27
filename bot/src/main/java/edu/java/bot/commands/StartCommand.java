package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "зарегистрировать пользователя";
    }

    @Override
    public SendMessage handle(Update update) {
        //TODO: проверка на то, что пользователь уже был зарегистрирован ранее
        return new SendMessage(update.message().chat().id(), """
            Вы были зарегистрированы! Приветствую, я бот для отслеживания ссылок.
            Для получения списка команд, введите /help.
            """);
    }
}
