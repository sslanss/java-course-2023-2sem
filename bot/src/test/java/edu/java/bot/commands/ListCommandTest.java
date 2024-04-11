package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.AbstractCommandTest;
import edu.java.commands.impls.ListCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ListCommandTest extends AbstractCommandTest {
    @Autowired
    ListCommand listCommand;

    private static final String EXPECTED_MESSAGE = "Список отслеживаемых вами ссылок:\n";

    @Test
    public void listCommandShouldReturnCorrectMessage() {
        Update update = mockUpdate();

        SendMessage sendMessage = listCommand.handle(update);

        Assertions.assertThat(sendMessage.getParameters().get("text")).isEqualTo(EXPECTED_MESSAGE);
    }
}
