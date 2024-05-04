package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.AbstractCommandTest;
import edu.java.commands.impls.TrackCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TrackCommandTest extends AbstractCommandTest {
    @Autowired
    TrackCommand trackCommand;

    private static final String EXPECTED_REQUEST_MESSAGE = """
        Я поддерживаю следующие ресурсы: STACKOVERFLOW, GITHUB.
        Пришлите ссылку на вопрос в формате:"https://stackoverflow.com/questions/номер-вопроса"
        или на репозиторий в формате:"https://github.com/владелец-репозитория/название-репозитория".
        """;

    private static final String EXPECTED_SUCCESSFUL_RESPONSE_MESSAGE =
        "Ссылка была добавлена в список отслеживания. Теперь вы будете получать ее обновления.";

    @Test
    public void trackCommandShouldReturnCorrectRequestMessage() {
        Update update = mockUpdate();

        SendMessage sendMessage = trackCommand.handle(update);

        Assertions.assertThat(sendMessage.getParameters().get("text")).isEqualTo(EXPECTED_REQUEST_MESSAGE);
    }

    @Test
    public void trackCommandShouldReturnCorrectResponseMessage() {
        Update update = mockUpdate();

        SendMessage sendMessage = trackCommand.handleLink(update);

        Assertions.assertThat(sendMessage.getParameters().get("text")).isEqualTo(EXPECTED_SUCCESSFUL_RESPONSE_MESSAGE);
    }
}
