package edu.java.commands.impls;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperApiErrorException;
import edu.java.bot.client.ScrapperClient;
import edu.java.commands.Command;
import edu.java.commands.ReplyCommand;
import edu.java.responses.LinkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command, ReplyCommand {

    private final ScrapperClient scrapperClient;

    private static final String TRACK_MESSAGE = """
        Я поддерживаю следующие ресурсы: STACKOVERFLOW, GITHUB.
        Пришлите ссылку на вопрос в формате:"https://stackoverflow.com/questions/номер-вопроса"
        или на репозиторий в формате:"https://github.com/владелец-репозитория/название-репозитория".
        """;

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
        return new SendMessage(update.message().chat().id(), TRACK_MESSAGE);
    }

    @Override
    public SendMessage handleLink(Update update) {
        String trackLinkResult = trackLink(update.message().chat().id(), update.message().text());
        return new SendMessage(update.message().chat().id(), trackLinkResult);
    }

    private String trackLink(Long id, String link) {
        try {
            LinkResponse response = scrapperClient.trackLink(id, link);
            //где лучше всего проверять валидность ссылки?
        } catch (ScrapperApiErrorException ex) {
            return ex.getDescription();
        }
        return "Ссылка была добавлена в список отслеживания. Теперь вы будете получать ее обновления.";
    }
}
