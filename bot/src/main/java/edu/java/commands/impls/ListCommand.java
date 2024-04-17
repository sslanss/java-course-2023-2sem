package edu.java.commands.impls;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperApiException;
import edu.java.bot.client.ScrapperClient;
import edu.java.commands.Command;
import edu.java.responses.ListLinksResponse;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private final ScrapperClient scrapperClient;

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
        String trackedLinksResult = getAllTrackedLinks(update.message().chat().id());
        return new SendMessage(update.message().chat().id(), trackedLinksResult);
    }

    private String getAllTrackedLinks(Long id) {
        try {
            ListLinksResponse trackedLinks = scrapperClient.getLinks(id);
            if (trackedLinks == null || trackedLinks.getLinks().isEmpty()) {
                return "Список отслеживаемых вами ссылок пуст.";
            } else {
                return "Список отслеживаемых вами ссылок:\n" + trackedLinks.getLinks()
                    .stream()
                    .map(linkResponse -> linkResponse.getUrl().toString())
                    .collect(Collectors.joining("\n"));
            }
        } catch (ScrapperApiException ex) {
            return ex.getDescription() + "Для того, чтобы отслеживать ссылки, зарегистрируйтесь.";
        }
    }
}
