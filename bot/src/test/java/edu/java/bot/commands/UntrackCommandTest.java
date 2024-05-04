package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.AbstractCommandTest;
import edu.java.commands.impls.UntrackCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.mockito.Mockito.when;

public class UntrackCommandTest extends AbstractCommandTest {
    @Autowired
    UntrackCommand untrackCommand;

    private static final String EXPECTED_REQUEST_MESSAGE = "Введите ссылку для прекращения отслеживания:";

    private static final String EXPECTED_SUCCESSFUL_RESPONSE_MESSAGE =
        "Ссылка была удалена из списка отслеживания. Вы больше не будете получать ее обновления.";

    @Test
    public void trackCommandShouldReturnCorrectRequestMessage() {
        Update update = mockUpdate();

        SendMessage sendMessage = untrackCommand.handle(update);

        Assertions.assertThat(sendMessage.getParameters().get("text")).isEqualTo(EXPECTED_REQUEST_MESSAGE);
    }

    @Test
    public void trackCommandShouldReturnCorrectResponseMessage() {
        Update update = mockUpdate();
        when(update.message().text()).thenReturn("https://stackoverflow.com/questions/100");

        SendMessage sendMessage = untrackCommand.handleLink(update);

        Assertions.assertThat(sendMessage.getParameters().get("text")).isEqualTo(EXPECTED_SUCCESSFUL_RESPONSE_MESSAGE);
    }
}
