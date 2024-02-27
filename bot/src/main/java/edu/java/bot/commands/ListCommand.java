package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    private final LinkService service;

    @Autowired
    public ListCommand(LinkService service) {
        this.service = service;
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        String commandResult = service.list(update.message().chat().id());
        return new SendMessage(update.message().chat().id(), commandResult);
    }
}
