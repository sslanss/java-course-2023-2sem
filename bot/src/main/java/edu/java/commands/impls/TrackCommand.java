package edu.java.commands.impls;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.commands.Command;
import edu.java.commands.ReplyCommand;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command, ReplyCommand {

    //TODO:ScrapperClient

    private static final String TRACK_MESSAGE = """
        Я поддерживаю следующие ресурсы: STACKOVERFLOW, GITHUB.
        Пришлите ссылку на вопрос в формате:"https://stackoverflow.com/questions/номер-вопроса"
        или на репозиторий в формате:"https://github.com/владелец-репозитория/название-репозитория".
        Введите ссылку для начала отслеживания ее обновлений:
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
        //TODO:ScrapperClient.trackLink(update.message().chat().id(), update.message().text())
        return new SendMessage(update.message().chat().id(), """
            Ссылка была добавлена в список отслеживания. Теперь вы будете получать ее обновления.""");
    }
}
