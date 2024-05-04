package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.AbstractCommandTest;
import edu.java.commands.impls.HelpCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HelpCommandTest extends AbstractCommandTest {
    @Autowired
    HelpCommand helpCommand;

    private static  final String EXPECTED_MESSAGE = """
        Список доступных команд:
        /help - вывести спиок доступных команд
        /list - показать список отслеживаемых ссылок
        /start - зарегистрировать пользователя
        /track - начать отслеживание ссылки
        /untrack - прекратить отслеживание ссылки""";

    @Test
    public void helpCommandShouldReturnCorrectMessage(){
        Update update = mockUpdate();

        SendMessage sendMessage = helpCommand.handle(update);

        Assertions.assertThat(sendMessage.getParameters().get("text")).isEqualTo(EXPECTED_MESSAGE);
    }
}
