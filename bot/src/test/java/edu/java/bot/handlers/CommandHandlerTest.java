package edu.java.bot.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.AbstractCommandTest;
import edu.java.commands.impls.HelpCommand;
import edu.java.handlers.CommandHandler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.mockito.Mockito.when;

public class CommandHandlerTest extends AbstractCommandTest {
    @Autowired
    CommandHandler commandHandler;

    @Test
    public void commandHandlerShouldCorrectlyProcessCommandUpdate() {
        Update update = mockUpdate();
        when(update.message().text()).thenReturn("/help");

        commandHandler.tryProcessCommand(update);

        Assertions.assertThat(commandHandler.getLastPulledCommand().getClass()).isEqualTo(HelpCommand.class);
    }

    @Test
    public void commandHandlerShouldCorrectlyProcessIncorrectUpdate() {
        Update update = mockUpdate();
        when(update.message().text()).thenReturn("////");

        SendMessage sendMessage = commandHandler.tryProcessCommand(update);

        Assertions.assertThat(sendMessage.getParameters().get("text")).isEqualTo("Команда не распознана!");
    }
}
