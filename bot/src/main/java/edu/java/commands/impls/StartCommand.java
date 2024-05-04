package edu.java.commands.impls;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.commands.Command;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {

    //TODO:ScrapperClient

    private static final String START_MESSAGE = """
        Приветствую, я бот для отслеживания обновлений по заданным ссылкам.
        Я поддерживаю следующие ресурсы: STACKOVERFLOW, GITHUB.
        Для STACKOVERFLOW пришлите ссылку на вопрос, чтобы получать на него новые ответы.
        Для GITHUB пришлите ссылку на репозиторий, чтобы получать его обновления(создание/удаление веток,
        отправление изменений).
        Для полного получения списка команд, введите /help.
        """;

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
        String registrationResult = registerChat(update.message().chat().id());
        return new SendMessage(update.message().chat().id(), registrationResult + START_MESSAGE);
    }

    private String registerChat(Long id) {
        //TODO: String registrationResult = ScraperClient.registerChat(id);
        return "Вы были зарегистрированы! ";
    }
}
