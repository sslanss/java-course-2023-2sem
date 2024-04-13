package edu.java.handlers;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.LinkTrackerBot;
import edu.java.commands.Command;
import edu.java.commands.ReplyCommand;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdatesHandler implements UpdatesListener {

    private final LinkTrackerBot bot;

    private final CommandHandler commandHandler;

    private final ReplyCommandHandler replyCommandHandler;

    public UpdatesHandler(LinkTrackerBot bot, CommandHandler commandHandler, ReplyCommandHandler replyCommandHandler) {
        this.bot = bot;
        this.commandHandler = commandHandler;
        this.replyCommandHandler = replyCommandHandler;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startHandleUpdates() {
        bot.start(this, e -> {
            if (e.response() != null) {
                log.error("Telegram API error - Code: {}, Description: {}", e.response().errorCode(),
                    e.response().description()
                );
            } else {
                log.error("Network error", e);
            }
        });
    }

    @Override
    public int process(List<Update> updates) {
        for (var update : updates) {
            Command previousCommand = commandHandler.getLastPulledCommand();
            if (previousCommand instanceof ReplyCommand) {
                SendMessage message =
                    replyCommandHandler.tryProcessReplyCommand((ReplyCommand) previousCommand, update);
                bot.execute(message);
                commandHandler.setLastPulledCommand(null);
            } else {
                SendMessage message = commandHandler.tryProcessCommand(update);
                bot.execute(message);
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}

