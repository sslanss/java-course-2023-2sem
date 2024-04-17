package edu.java.commands.impls;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperApiException;
import edu.java.bot.client.ScrapperClient;
import edu.java.commands.Command;
import edu.java.commands.ReplyCommand;
import edu.java.responses.LinkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command, ReplyCommand {

    private final ScrapperClient scrapperClient;

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
        String untrackLinkResult = untrackLink(update.message().chat().id(), update.message().text());
        return new SendMessage(update.message().chat().id(), untrackLinkResult);
    }

    private String untrackLink(Long id, String link) {
        try {
            LinkResponse response = scrapperClient.untrackLink(id, link);
            //где лучше всего проверять валидность ссылки?
        } catch (ScrapperApiException ex) {
            return ex.getDescription();
        }
        return "Ссылка была удалена из списка отслеживания. Вы больше не будете получать ее обновления.";
    }
}
