package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.AbstractCommandTest;
import edu.java.commands.impls.StartCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StartCommandTest extends AbstractCommandTest {
    @Autowired
    StartCommand startCommand;

    private static final String EXPECTED_MESSAGE = """
        Вы были зарегистрированы! Приветствую, я бот для отслеживания обновлений по заданным ссылкам.
        Я поддерживаю следующие ресурсы: STACKOVERFLOW, GITHUB.
        Для STACKOVERFLOW пришлите ссылку на вопрос, чтобы получать на него новые ответы.
        Для GITHUB пришлите ссылку на репозиторий, чтобы получать его обновления(создание/удаление веток,
        отправление изменений).
        Для полного получения списка команд, введите /help.
        """;

    @Test
    public void startCommandShouldWorkCorrectly() {
        Update update = mockUpdate();

        SendMessage sendMessage = startCommand.handle(update);

        Assertions.assertThat(sendMessage.getParameters().get("text")).isEqualTo(EXPECTED_MESSAGE);
    }
}
