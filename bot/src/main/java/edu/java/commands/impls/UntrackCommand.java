package edu.java.commands.impls;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.commands.Command;
import edu.java.commands.ReplyCommand;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command, ReplyCommand {

    //TODO:ScrapperClient

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "прекратить отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), "Введите ссылку для прекращения отслеживания:");
    }

    @Override
    public SendMessage handleLink(Update update) {
        //TODO:ScrapperClient.untrackLink(update.message().chat().id(), update.message().text())
        return new SendMessage(update.message().chat().id(), """
            Ссылка была удалена из списка отслеживания. Вы больше не будете получать ее обновления.""");
    }
}
