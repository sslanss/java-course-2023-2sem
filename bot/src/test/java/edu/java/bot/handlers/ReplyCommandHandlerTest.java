package edu.java.bot.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.AbstractCommandTest;
import edu.java.commands.ReplyCommand;
import edu.java.handlers.ReplyCommandHandler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReplyCommandHandlerTest extends AbstractCommandTest {
    @Autowired
    ReplyCommandHandler replyCommandHandler;

    protected ReplyCommand mockReplyCommand() {
        return mock(ReplyCommand.class);
    }

    @Test
    public void commandHandlerShouldCorrectlyProcessIncorrectUpdate() {
        Update update = mockUpdate();
        ReplyCommand replyCommand = mockReplyCommand();
        when(update.message().text()).thenReturn("/help");

        SendMessage sendMessage = replyCommandHandler.tryProcessReplyCommand(replyCommand, update);

        Assertions.assertThat(sendMessage.getParameters().get("text")).isEqualTo("Вы не ввели ссылку!");
    }
}
