package edu.java.commands.impls;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.commands.Command;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    //TODO:ScrapperClient
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
        String commandResult = getAllTrackedLinks();
        return new SendMessage(update.message().chat().id(), commandResult);
    }

    public String getAllTrackedLinks() {
        //TODO: ScrapperClient.listAllTrackedLinks
        return "Список отслеживаемых вами ссылок:\n";
    }
}
