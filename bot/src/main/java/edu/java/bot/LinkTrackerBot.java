package edu.java.bot;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LinkTrackerBot implements Bot {
    private final TelegramBot bot;

    public LinkTrackerBot(TelegramBot bot) {
        this.bot = bot;
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
    public void start(UpdatesListener listener, ExceptionHandler handler) {
        bot.setUpdatesListener(listener, handler);
        log.info("Bot successfully started.");
    }

    @Override
    public void close() {

    }
}


