package edu.java.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.commands.ReplyCommand;
import org.springframework.stereotype.Component;

@Component
public class ReplyCommandHandler {
    public SendMessage tryProcessReplyCommand(ReplyCommand command, Update update) {
        if (command != null && isReply(update)) {
            return command.handleLink(update);
        }
        return new SendMessage(update.message().chat().id(), "Вы не ввели ссылку!");
    }

    private boolean isReply(Update update) {
        return !update.message().text().startsWith("/");
    }
}
