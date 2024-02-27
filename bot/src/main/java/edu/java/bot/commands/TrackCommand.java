package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {
    private final LinkService service;

    @Autowired
    public TrackCommand(LinkService service) {
        this.service = service;
    }

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
