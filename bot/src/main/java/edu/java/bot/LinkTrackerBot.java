package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.repositories.LinkRepository;
import edu.java.bot.services.CommandService;
import edu.java.bot.services.LinkService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinkTrackerBot implements Bot {
    private final TelegramBot bot;
    private final CommandService service;

    public LinkTrackerBot(ApplicationConfig config) {
        bot = new TelegramBot(config.telegramToken());
        //здесь исправить
        service = new CommandService(new LinkService(new LinkRepository()));
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        try {
            bot.execute(request);
        } catch (Exception e) {
            log.error("Error response - Description: {}", e.getMessage());
        }
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach((update) -> {
            SendMessage message = service.processUpdate(update);
            execute(message);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void start() {
        bot.setUpdatesListener(this, e -> {
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
    public void close() {
    }
}
