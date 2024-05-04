package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public abstract class AbstractCommandTest {
    @MockBean
    TelegramBot bot;

    @MockBean
    protected ScrapperClient scrapperClient;

    protected Update mockUpdate() {
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        Message message = mock(Message.class);

        when(chat.id()).thenReturn(1L);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);

        return update;
    }
}
