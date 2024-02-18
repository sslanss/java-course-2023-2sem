package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.List;

public class LinkTrackerBot implements Bot {

    private final TelegramBot bot;
    private final CommandService service;

    public LinkTrackerBot(ApplicationConfig config) {
        bot = new TelegramBot(config.telegramToken());
        service = new CommandService();
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        try {
            bot.execute(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update:updates){
            System.out.println(update.message().text());
        }
        return 0;
    }

    @Override
    public void start() {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                var response = service.processUpdate(update);
                execute(response);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });//обработка ошибок?
    }

    @Override
    public void close() {

    }
}
